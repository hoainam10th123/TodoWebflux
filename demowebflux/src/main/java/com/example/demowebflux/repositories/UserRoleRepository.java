package com.example.demowebflux.repositories;

import com.example.demowebflux.entities.UserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Integer> {
    Flux<UserRole> findRolesByUserId(int userId);
}