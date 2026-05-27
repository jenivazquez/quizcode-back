package com.quizcode.module.participation.domain.entity.answer;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class SavedAnswer {

    private final Answer answer;

    public SavedAnswer(String questionId, List<String> codeOptions, String writtenAnswer, Boolean isCorrect, Integer score, String feedback) {
        validate(questionId);
        this.answer = new Answer(questionId, codeOptions, writtenAnswer, isCorrect, score, feedback);
    }

    private void validate(String questionId) {
        ValidatorUtil.validateFieldsNotNullAsGroup(Arrays.asList(questionId));
    }
}
