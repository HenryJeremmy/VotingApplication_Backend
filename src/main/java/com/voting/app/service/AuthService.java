package com.voting.app.service;

import com.voting.app.dto.request.LoginRequest;
import com.voting.app.dto.request.RegisterRequest;
import com.voting.app.dto.response.JwtResponse;
import com.voting.app.dto.response.MessageResponse;
import com.voting.app.model.ERole;
import com.voting.app.model.Role;
import com.voting.app.model.User;
import com.voting.app.repository.RoleRepository;
import com.voting.app.repository.UserRepository;
import com.voting.app.security.jwt.JwtService;
import com.voting.app.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    // Authenticate user and generate JWT token
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticateUserCredentials(loginRequest);

        // Set authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtService.generateToken(authentication.getName());

        // Extract roles and return the response
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                userDetails.getId().toString(),
                userDetails.getName(),
                userDetails.getEmail(),
                jwt,
                roles);
    }

    // Register a new user
    public MessageResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = createUser(registerRequest);
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    // Authenticate user credentials using AuthenticationManager
    private Authentication authenticateUserCredentials(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
    }

    // Create a user entity from register request
    private User createUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        // Assign a default role to the new user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        return user;
    }
}
