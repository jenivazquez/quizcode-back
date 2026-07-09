package com.quizcode.module.participation.api.dto.participation;

import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPartResponse {
    private String id;
    private ParticipationStatus status;
    private String token;
    private Instant validUntil;
}