package com.example.demowebflux.services;

import com.example.demowebflux.dtos.Pagination;
import com.example.demowebflux.dtos.TodoDto;
import com.example.demowebflux.entities.Todo;
import com.example.demowebflux.exceptions.NotFoundException;
import com.example.demowebflux.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public Flux<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Mono<Pagination<TodoDto>> findByCompleted(int pageNumber, int pageSize, boolean completed) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return todoRepository.findByCompleted(completed, pageable)
                .collectList()
                .flatMap(todos ->todoRepository.countByCompleted(completed).map(count -> {
                    List<TodoDto> todosMapped = todos.stream()
                            .map(todo -> modelMapper.map(todo, TodoDto.class))
                            .toList();
                    return new Pagination<TodoDto>(pageNumber, pageSize, count, todosMapped);
                }));
    }

    public Mono<Pagination<TodoDto>> getTodoPagination(int pageNumber, int pageSize, String title) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return todoRepository.findByTitleContaining(title, pageable)
                .collectList()
                .flatMap(todos ->
                        todoRepository.countByTitle(title)
                                .map(count -> {
                                    List<TodoDto> todosMapped = todos.stream()
                                            .map(todo -> modelMapper.map(todo, TodoDto.class))
                                            .toList();
                                    return new Pagination<TodoDto>(pageNumber, pageSize, count, todosMapped);
                                })
                );
    }


    public Mono<Todo> findById(int id) {
        return todoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("can not find id: "+ id)));
    }

    public Mono<Object> deleteById(int id) {
        return findById(id)
                .flatMap(todoData ->{
                    Map<String, Integer> map = new HashMap<>();
                    map.put("id", id);
                    return todoRepository.deleteById(id)
                            .then(Mono.just(map));
                        }
                );
    }

    public Mono<TodoDto> addTodo(TodoDto data){
        Todo todo = modelMapper.map(data, Todo.class);
        return todoRepository.save(todo).map(todoData -> modelMapper.map(todoData, TodoDto.class));
    }

    public Mono<TodoDto> update(int id, TodoDto data){
        Mono<Todo> tempTodo = findById(id);
        return tempTodo.flatMap(todo -> {
            todo.setUsername(data.getUsername());
            todo.setCompleted(data.isCompleted());
            todo.setTitle(data.getTitle());
            todo.setDescription(data.getDescription());

            return todoRepository.save(todo)
                    .map(todoData -> modelMapper.map(todoData, TodoDto.class));
        });
    }
}
