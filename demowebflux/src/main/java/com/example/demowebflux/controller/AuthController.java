package com.example.demowebflux.controller;

import com.example.demowebflux.dtos.LoginDto;
import com.example.demowebflux.dtos.UserDto;
import com.example.demowebflux.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> login(@Valid @RequestBody Mono<LoginDto> data) {
        return data.flatMap(userService::login);
    }

}
