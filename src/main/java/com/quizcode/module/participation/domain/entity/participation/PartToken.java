package com.quizcode.module.participation.domain.entity.participation;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class PartToken {
    private String token;
    private Instant validUntil;
}