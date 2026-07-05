package com.quizcode.module.participation.domain.entity.answer;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class NewAnswer {

    private final Answer answer;

    public NewAnswer(String questionId, List<String> codeOptions, String writtenAnswer) {
        validate(questionId);
        this.answer = new Answer(questionId, codeOptions, writtenAnswer, null, null, null);
    }

    private void validate(String questionId) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la pregunta", questionId);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
