package com.quizcode.module.question.api.dto.question;

import com.quizcode.module.question.api.dto.option.OptionToAnswerResponse;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionToAnswerResponse {
    private String id;
    private String statement;
    private String baseCode;
    private QuestionType type;
    private Integer order;
    private Integer score;
    private List<OptionToAnswerResponse> options;
}
