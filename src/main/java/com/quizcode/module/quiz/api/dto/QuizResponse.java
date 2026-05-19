package com.quizcode.module.quiz.api.dto;

import com.quizcode.module.quiz.domain.entity.QuizStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private Boolean hasLimit;
    private Integer limitMinutes;
    private QuizStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
