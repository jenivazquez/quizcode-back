package com.quizcode.module.participation.api.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerReviewRequest {
    private String questionId;
    private Boolean isCorrect;
    private Integer score;
    private String feedback;
}
