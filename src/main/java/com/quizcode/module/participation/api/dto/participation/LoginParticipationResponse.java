package com.quizcode.module.participation.api.dto.participation;

import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParticipationResponse {
    private String id;
    private ParticipationStatus status;
}