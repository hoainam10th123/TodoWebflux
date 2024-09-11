package com.example.demowebflux.repositories;

import com.example.demowebflux.entities.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TodoRepository extends ReactiveCrudRepository<Todo, Integer> {
    Flux<Todo> findByTitleContaining(String title, Pageable pageable);

    Flux<Todo> findByCompleted(boolean completed, Pageable pageable);

    @Query("SELECT COUNT(*) FROM todos WHERE completed = :completed")
    Mono<Long> countByCompleted(boolean completed);

    @Query("SELECT COUNT(*) FROM todos WHERE title LIKE concat('%',:title,'%')")
    Mono<Long> countByTitle(String title);

    Flux<Todo> findByUsername(String username);
}
