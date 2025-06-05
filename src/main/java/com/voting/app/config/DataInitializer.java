
package com.voting.app.config;

import com.voting.app.model.Candidate;
import com.voting.app.model.ERole;
import com.voting.app.model.Role;
import com.voting.app.model.User;
import com.voting.app.repository.CandidateRepository;
import com.voting.app.repository.RoleRepository;
import com.voting.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        initRoles();
        
        // Create admin user if it doesn't exist
        createAdminUser();
        
        // Create sample candidates if none exist
        createSampleCandidates();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByEmail("admin@voting.com")) {
            User admin = new User();
            admin.setName("Administrator");
            admin.setEmail("admin@voting.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role not found.")));
            roles.add(roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: User Role not found.")));
            
            admin.setRoles(roles);
            userRepository.save(admin);
        }
    }

    private void createSampleCandidates() {
        if (candidateRepository.count() == 0) {
            // Create sample candidates
            Candidate candidate1 = new Candidate();
            candidate1.setName("Bola Tinubu");
            candidate1.setParty("APC");
            candidate1.setPosition("President");
            candidate1.setImageUrl("https://lh3.googleusercontent.com/Qxgqq7C6s0F6syOCGcLnpIGyTkykAoZbvV6M2SKBJqGW2eEqEp17zpW19UPrzO7J7EOY9dM=s85");
            candidateRepository.save(candidate1);
            
            Candidate candidate2 = new Candidate();
            candidate2.setName("Peter Obi");
            candidate2.setParty("Labour Party");
            candidate2.setPosition("President");
            candidate2.setImageUrl("https://i0.wp.com/media.premiumtimesng.com/wp-content/files/2022/10/78f1dc4e-142f-44e4-a328-f15724fe63d4_peter-obi.jpg?fit=2252%2C1312&ssl=1");
            candidateRepository.save(candidate2);
            
            Candidate candidate3 = new Candidate();
            candidate3.setName("Atiku Abubaka");
            candidate3.setParty("Independent");
            candidate3.setPosition("President");
            candidate3.setImageUrl("https://i0.wp.com/media.premiumtimesng.com/wp-content/files/2022/03/E2lCUSsXEAIViLc.jpg?fit=2256%2C1504&ssl=1");
            candidateRepository.save(candidate3);
        }
    }
}
