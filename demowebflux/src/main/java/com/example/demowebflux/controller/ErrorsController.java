package com.example.demowebflux.controller;

import com.example.demowebflux.dtos.LoginDto;
import com.example.demowebflux.exceptions.BadRequestException;
import com.example.demowebflux.exceptions.ForbiddenException;
import com.example.demowebflux.exceptions.NotFoundException;
import com.example.demowebflux.exceptions.UnAuthorizedException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RequestMapping("/api/errors")
@RestController
public class ErrorsController {
    @GetMapping("not-found")
    public Mono<Object> get404NotFound(){
        throw new NotFoundException("Khong thay object");
    }

    @GetMapping("bad-request")
    public Mono<Object> get400BadRequest(){
        throw new BadRequestException("khong tim thay id");
    }

    @GetMapping("unauthorized")
    public Mono<Object> get401Unauthorized(){
        throw new UnAuthorizedException("Ban Chua dang nhap");
    }

    @GetMapping("forbidden")
    public Mono<Object> get403Forbidden(){
        throw new ForbiddenException("Ban khong co quyen truy cap tai nguyen nay");
    }

    @GetMapping("server-error")
    public Mono<Object> get500ServerError() {
        throw new RuntimeException("500 server-error");
    }

    @PostMapping("validation")
    public Mono<Object>  get400Validation(@Valid @RequestBody LoginDto todo) {
        return  Mono.just("Validate success");
    }
}
