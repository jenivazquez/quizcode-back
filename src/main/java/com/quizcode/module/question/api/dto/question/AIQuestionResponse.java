package com.quizcode.module.question.api.dto.question;

import com.quizcode.module.question.api.dto.option.AIOptionResponse;
import com.quizcode.module.question.api.dto.option.OptionResponse;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIQuestionResponse {
    private String message;
    private String statement;
    private String baseCode;
    private QuestionType type;
    private Integer score;
    private List<AIOptionResponse> options;
}
