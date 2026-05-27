package com.quizcode.module.participation.domain.entity.answer;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.Util;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class NewAnswer {

    private final Answer answer;

    public NewAnswer(String questionId, List<String> codeOptions, String writtenAnswer) {
        validate(questionId, codeOptions, writtenAnswer);
        this.answer = new Answer(questionId, codeOptions, writtenAnswer, null, null, null);
    }

    private void validate(String questionId, List<String> codeOptions, String writtenAnswer) {
        validateRequiredFields(questionId);
        validateAnswerContent(codeOptions, writtenAnswer);
    }

    private void validateRequiredFields(String questionId) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la pregunta", questionId);
        ValidatorUtil.validateFieldsNotNull(fields);
    }

    private void validateAnswerContent(List<String> codeOptions, String writtenAnswer) {
        if (Util.isNull(codeOptions) && Util.isNull(writtenAnswer)) {
            throw new InvalidDataExceptionCustom("La respuesta debe incluir opciones seleccionadas o una respuesta escrita");
        }
    }
}
