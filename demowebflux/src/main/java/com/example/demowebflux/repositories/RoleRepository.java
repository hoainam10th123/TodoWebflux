package com.example.demowebflux.repositories;

import com.example.demowebflux.entities.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Integer> {
    Mono<Role> findByName(String name);
}
