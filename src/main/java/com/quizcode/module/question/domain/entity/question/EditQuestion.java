package com.quizcode.module.question.domain.entity.question;

import com.quizcode.module.question.domain.entity.option.Option;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EditQuestion {

    private final Question question;

    public EditQuestion(String id, String quizId, String statement, String baseCode, QuestionType type, Integer order, Integer score, List<Option> options) {
        validate(id, quizId, statement, type, order, score);
        this.question = new Question(id, statement, baseCode, type, order, score, quizId, options);
    }

    private void validate(String id, String quizId, String statement, QuestionType type, Integer order, Integer score) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la pregunta", id);
        fields.put("cuestionario", quizId);
        fields.put("enunciado", statement);
        fields.put("tipo", type);
        fields.put("orden", order);
        fields.put("puntuación", score);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
