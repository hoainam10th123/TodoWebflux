package com.example.demowebflux.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message){super(message);}
}
