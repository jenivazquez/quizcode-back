package com.quizcode.module.participation.domain.entity.question;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionSummary {

    private final String id;
    private final String type;
    private final List<String> validOptionCodes;
    private final Integer score;

    public QuestionSummary(String id, String type, List<String> validOptionCodes, Integer score) {
        this.id = id;
        this.type = type;
        this.validOptionCodes = validOptionCodes != null ? validOptionCodes : List.of();
        this.score = score;
    }
}
