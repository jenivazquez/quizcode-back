package com.quizcode.module.question.domain.entity.question;

import com.quizcode.module.question.domain.entity.option.Option;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class NewQuestion {

    private final Question question;

    public NewQuestion(String quizId, String statement, String baseCode, QuestionType type, Integer order, Integer score, List<Option> options) {
        validate(quizId, statement, type, order, score);
        this.question = new Question(null, statement, baseCode, type, order, score, quizId, options);
    }

    private void validate(String quizId, String statement, QuestionType type, Integer order, Integer score) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("cuestionario", quizId);
        fields.put("enunciado", statement);
        fields.put("tipo", type);
        fields.put("orden", order);
        fields.put("puntuación", score);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
