package com.quizcode.module.question.domain.entity.question;

import com.quizcode.module.question.domain.entity.option.Option;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class SavedQuestion {

    private final Question question;

    public SavedQuestion(String id, String quizId, String statement, String baseCode, QuestionType type, Integer order, Integer score, List<Option> options) {
        validate(id, quizId, statement, type, order, score);
        this.question = new Question(id, statement, baseCode, type, order, score, quizId, options);
    }

    private void validate(String id, String quizId, String statement, QuestionType type, Integer order, Integer score) {
        ValidatorUtil.validateFieldsNotNullAsGroup(Arrays.asList(id, quizId, statement, type, order, score));
    }
}
