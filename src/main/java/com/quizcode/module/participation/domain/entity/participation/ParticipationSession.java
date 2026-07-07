package com.quizcode.module.participation.domain.entity.participation;

import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ParticipationSession {
    private String id;
    private ParticipationStatus status;
    private String token;
    private Instant validUntil;
}
