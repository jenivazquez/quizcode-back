package com.quizcode.module.quiz.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizRequest {
    private String title;
    private String description;
    private Boolean hasLimit;
    private Integer limitMinutes;
}