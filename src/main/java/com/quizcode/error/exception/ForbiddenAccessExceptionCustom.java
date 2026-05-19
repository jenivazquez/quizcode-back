package com.quizcode.error.exception;

public class ForbiddenAccessExceptionCustom extends RuntimeException{
    public ForbiddenAccessExceptionCustom(String message){
        super(message);
    }
}
