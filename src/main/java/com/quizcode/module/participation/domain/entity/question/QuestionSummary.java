package com.quizcode.module.participation.domain.entity.question;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionSummary {

    private final String id;
    private final QuestionType type;
    private final String statement;
    private final String baseCode;
    private final List<String> validOptionCodes;
    private final Integer score;

    public QuestionSummary(String id, QuestionType type, String statement, String baseCode, List<String> validOptionCodes, Integer score) {
        this.id = id;
        this.type = type;
        this.statement = statement;
        this.baseCode = baseCode;
        this.validOptionCodes = validOptionCodes != null ? validOptionCodes : List.of();
        this.score = score;
    }
}
