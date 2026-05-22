package com.quizcode.error.api;

import com.quizcode.error.api.dto.ErrorResponse;
import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.ForbiddenAccessExceptionCustom;
import com.quizcode.error.exception.InvalidCredentialsExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataExceptionCustom.class)
    ResponseEntity<ErrorResponse> handleInvalidData(InvalidDataExceptionCustom exception) {
        return getResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundExceptionCustom.class)
    ResponseEntity<ErrorResponse> handleNotFound(NotFoundExceptionCustom exception) {
        return getResponseEntity(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsExceptionCustom.class)
    ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsExceptionCustom exception) {
        return getResponseEntity(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenAccessExceptionCustom.class)
    ResponseEntity<ErrorResponse> handleForbiddenAccess(ForbiddenAccessExceptionCustom exception) {
        return getResponseEntity(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidStatusExceptionCustom.class)
    ResponseEntity<ErrorResponse> handleInvalidStatus(InvalidStatusExceptionCustom exception) {
        return getResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AutoGenerationExceptionCustom.class)
    ResponseEntity<ErrorResponse> handleAutoGeneration(AutoGenerationExceptionCustom exception) {
        return getResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException exception) {
        return getResponseEntity("Uno de los campos tiene un valor que no corresponde con sus valores permitidos", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException exception) {
        return getResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> getResponseEntity(String message, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.builder()
                        .code(httpStatus.getReasonPhrase())
                        .message(message)
                        .timestamp(Instant.now())
                        .build());
    }

    private ResponseEntity<ErrorResponse> getResponseEntity(Exception exception, HttpStatus httpStatus) {
        return getResponseEntity(exception.getMessage(), httpStatus);
    }
}