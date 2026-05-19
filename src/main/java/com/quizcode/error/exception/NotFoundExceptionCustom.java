package com.quizcode.error.exception;

public class NotFoundExceptionCustom extends RuntimeException{
    public NotFoundExceptionCustom(String message){
        super(message);
    }
}
