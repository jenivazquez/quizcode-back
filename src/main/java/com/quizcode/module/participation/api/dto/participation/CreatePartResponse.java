package com.quizcode.module.participation.api.dto.participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartResponse {
    private String id;
    private String token;
    private Instant validUntil;
}