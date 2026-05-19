package com.quizcode.error.exception;

public class InvalidCredentialsExceptionCustom extends RuntimeException{
    public InvalidCredentialsExceptionCustom(String message){
        super(message);
    }
}
