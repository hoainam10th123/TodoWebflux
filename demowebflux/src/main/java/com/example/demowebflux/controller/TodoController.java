package com.example.demowebflux.controller;

import com.example.demowebflux.dtos.Pagination;
import com.example.demowebflux.dtos.TodoDto;
import com.example.demowebflux.services.TodoService;
import com.example.demowebflux.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.security.Principal;

@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@RestController
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TodoDto> getById(@PathVariable int id) {
        return todoService.findById(id).map(todo -> new TodoDto(todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(), todo.getUsername()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Pagination<TodoDto>> getTodos(@RequestParam(required = false) int pageNumber,
                                                 @RequestParam(required = false) int pageSize,
                                                 @RequestParam(required = false) String title) {
            return todoService.getTodoPagination(pageNumber, pageSize, title);
    }

    @GetMapping("get-completed")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Pagination<TodoDto>> getByCompleted(@RequestParam(required = true) int pageNumber,
                                        @RequestParam(required = true) int pageSize,
                                        @RequestParam boolean completed) {
        return todoService.findByCompleted(pageNumber, pageSize, completed);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TodoDto> addTodo(@Valid @RequestBody Mono<TodoDto> data) {
        return userService.getCurrentUser().flatMap(user ->
                data.flatMap(todoDto -> {
            todoDto.setUsername(user.getUsername());
            return todoService.addTodo(todoDto);
        }));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TodoDto> updateTodo(@PathVariable int id,
                                    @Valid @RequestBody Mono<TodoDto> todoDto,
                                    Principal principal) {
        return todoDto.flatMap(data ->{
            data.setUsername(principal.getName());
            return todoService.update(id, data);
        });
    }

    @PreAuthorize("@todoOwner.isTodoOwner(#id)")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> deleteTodo(@PathVariable int id) {
        return todoService.deleteById(id);
    }
}
