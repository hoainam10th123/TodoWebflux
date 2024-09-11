package com.example.demowebflux.security;

import com.example.demowebflux.entities.Todo;
import com.example.demowebflux.exceptions.NotFoundException;
import com.example.demowebflux.repositories.TodoRepository;
import com.example.demowebflux.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TodoOwner {
    private final UserService userService;
    private final TodoRepository todoRepository;

    public Mono<Boolean> isTodoOwner(Integer id) {
        Mono<Todo> todoDb = todoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("can not find id: "+ id)));

        return userService.getCurrentUser()
                .flatMap(userDto ->todoDb.map(todo -> todo.getUsername().equals(userDto.getUsername())));
    }
}
