package com.example.demowebflux.exceptions;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message){super(message);}
}
