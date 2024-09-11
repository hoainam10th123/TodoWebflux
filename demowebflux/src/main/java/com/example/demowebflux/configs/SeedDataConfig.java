package com.example.demowebflux.configs;

import com.example.demowebflux.entities.Role;
import com.example.demowebflux.entities.User;
import com.example.demowebflux.entities.UserRole;
import com.example.demowebflux.repositories.RoleRepository;
import com.example.demowebflux.repositories.UserRepository;
import com.example.demowebflux.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class SeedDataConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedData(UserRepository userRepository,
                               RoleRepository roleRepository,
                               UserRoleRepository userRoleRepository) {
        return args -> {
            userRepository.count().subscribe(count ->{
                if(count == 0){
                    Role adminRole = new Role("ROLE_ADMIN");
                    Role userRole = new Role("ROLE_USER");

                    roleRepository.saveAll(Flux.just(adminRole, userRole)).subscribe();

                    User hoainam10th = new User(
                            "Nguyen Hoai Nam",
                            "hoainam10th", passwordEncoder.encode("123456"));

                    User ubuntu = new User(
                            "Ubuntu Nguyen",
                            "ubuntu", passwordEncoder.encode("123456"));

                    User admin = new User(
                            "Administrator",
                            "admin", passwordEncoder.encode("123456"));


                    userRepository.saveAll(Flux.just(admin, hoainam10th, ubuntu)).subscribe();

                    //find role

                    Mono<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");
                    Mono<Role> roleUser = roleRepository.findByName("ROLE_USER");


                    userRepository.findByUsername("admin")
                            .flatMap(adminUser -> roleAdmin.flatMap(role ->
                                    userRoleRepository.save(new UserRole(role.getId(), adminUser.getId()))
                            ))
                            .subscribe(savedUserRole -> {
                                System.out.println("UserRole saved: " + savedUserRole.getId());
                            }, error -> {
                                System.err.println("Error occurred: " + error.getMessage());
                            });

                    userRepository.findByUsername("hoainam10th")
                            .flatMap(hoainam10thUser -> roleUser.flatMap(role ->
                                    userRoleRepository.save(new UserRole(role.getId(), hoainam10thUser.getId()))
                            ))
                            .subscribe(savedUserRole -> {
                                System.out.println("UserRole saved: " + savedUserRole.getId());
                            }, error -> {
                                System.err.println("Error occurred: " + error.getMessage());
                            });

                    userRepository.findByUsername("ubuntu")
                            .flatMap(ubuntuUser -> roleUser.flatMap(role ->
                                    userRoleRepository.save(new UserRole(role.getId(), ubuntuUser.getId()))
                            ))
                            .subscribe(savedUserRole -> {
                                System.out.println("UserRole saved: " + savedUserRole.getId());
                            }, error -> {
                                System.err.println("Error occurred: " + error.getMessage());
                            });
                }
            });
        };
    }
}

