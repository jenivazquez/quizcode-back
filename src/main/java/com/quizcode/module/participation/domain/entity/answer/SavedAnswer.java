package com.quizcode.module.participation.domain.entity.answer;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.Util;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.List;

@Getter
public class SavedAnswer {

    private final Answer answer;

    public SavedAnswer(String questionId, List<String> codeOptions, String writtenAnswer, Boolean isCorrect, Integer score, String feedback) {
        validate(questionId, codeOptions, writtenAnswer);
        this.answer = new Answer(questionId, codeOptions, writtenAnswer, isCorrect, score, feedback);
    }

    private void validate(String questionId, List<String> codeOptions, String writtenAnswer) {
        ValidatorUtil.validateFieldsNotNullAsGroup(List.of(questionId));
        if (Util.isNull(codeOptions) && Util.isNull(writtenAnswer)) {
            throw new InvalidDataExceptionCustom("La respuesta debe incluir opciones seleccionadas o una respuesta escrita");
        }
    }
}
