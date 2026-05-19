package com.quizcode.module.quiz.api.dto;

import com.quizcode.module.quiz.domain.entity.QuizStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuizStatusRequest {
    private QuizStatus status;
}
