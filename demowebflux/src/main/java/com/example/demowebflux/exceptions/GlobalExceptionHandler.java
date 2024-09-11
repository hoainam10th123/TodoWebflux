package com.example.demowebflux.exceptions;

import com.example.demowebflux.errors.ApiException;
import com.example.demowebflux.errors.ApiResponse;
import com.example.demowebflux.errors.ApiValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Object>> handleRuntimeException(RuntimeException ex) {
        final String message = ex.getMessage();
        if(Objects.equals(message, "Invalid Credentials")){
            return Mono.just(new ResponseEntity<>(new ApiResponse(400, "Invalid password"), HttpStatus.BAD_REQUEST));
        }
        else if(Objects.equals(message, "Access Denied")){
            return Mono.just(new ResponseEntity<>(new ApiResponse(403, "You are not allow to do that"), HttpStatus.FORBIDDEN));
        }else{
            String stackTrace = Arrays.stream(ex.getStackTrace()).map(msg-> String.format("%s %n", msg.toString())).collect(Collectors.joining());
            return Mono.just(new ResponseEntity<>(new ApiException(500, ex.getMessage(), stackTrace), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<Object>> handleBadRequestException(BadRequestException ex) {
        return Mono.just(new ResponseEntity<>(new ApiResponse(400, ex.getMessage()), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Object>> handleValidationException(WebExchangeBindException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            //String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return Mono.just(new ResponseEntity<>(new ApiValidationErrorResponse(errors, ex.getMessage()), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<Object>> handleNotFoundException(NotFoundException ex) {
        return Mono.just(new ResponseEntity<>(new ApiResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public Mono<ResponseEntity<Object>> handleUnAuthorizedException(UnAuthorizedException ex) {
        return Mono.just(new ResponseEntity<>(new ApiResponse(401, ex.getMessage()), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(ForbiddenException.class)
    public Mono<ResponseEntity<Object>> handleForbiddenException(ForbiddenException ex) {
        return Mono.just(new ResponseEntity<>(new ApiResponse(403, ex.getMessage()), HttpStatus.FORBIDDEN));
    }
}
