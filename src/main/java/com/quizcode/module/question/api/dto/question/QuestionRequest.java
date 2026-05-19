package com.quizcode.module.question.api.dto.question;

import com.quizcode.module.question.api.dto.option.OptionRequest;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    private String statement;
    private String baseCode;
    private QuestionType type;
    private Integer order;
    private Integer score;
    private List<OptionRequest> options;
}
