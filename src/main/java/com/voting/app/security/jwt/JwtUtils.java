package com.voting.app.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

@Service
public class JwtUtils {

    private final JwtService jwtService;

    public JwtUtils(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }

    public String generateJwtToken(Authentication authentication) {
        return jwtService.generateToken(authentication.getName());
    }

    public String extractEmail(String token) {
        return jwtService.extractEmail(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtService.validateToken(token, userDetails);
    }
}
