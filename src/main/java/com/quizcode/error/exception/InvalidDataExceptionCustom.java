package com.quizcode.error.exception;

public class InvalidDataExceptionCustom extends RuntimeException{
    public InvalidDataExceptionCustom(String message){
        super(message);
    }
}
