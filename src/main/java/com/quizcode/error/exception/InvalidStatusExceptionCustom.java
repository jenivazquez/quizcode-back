package com.quizcode.error.exception;

public class InvalidStatusExceptionCustom extends RuntimeException {
    public InvalidStatusExceptionCustom(String message) {
        super(message);
    }
}