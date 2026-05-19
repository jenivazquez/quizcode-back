package com.quizcode.error.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {
    String code;
    String message;
    Instant timestamp;
}