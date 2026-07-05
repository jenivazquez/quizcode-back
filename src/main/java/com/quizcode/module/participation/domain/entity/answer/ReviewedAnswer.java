package com.quizcode.module.participation.domain.entity.answer;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ReviewedAnswer {

    private final Answer answer;

    public ReviewedAnswer(String questionId, Boolean isCorrect, Integer score, String feedback) {
        validate(questionId, isCorrect, score);
        this.answer = new Answer(questionId, null, null, isCorrect, score, feedback);
    }

    private void validate(String questionId, Boolean isCorrect, Integer score) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la pregunta asociada a la respuesta", questionId);
        fields.put("es correcta la respuesta", isCorrect);
        fields.put("puntuación de la respuesta", score);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
