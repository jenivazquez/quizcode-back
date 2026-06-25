package com.quizcode.module.participation.domain.entity.ai;

import lombok.Getter;

@Getter
public class AIReview {

    private final boolean isCorrect;
    private final int score;
    private final String feedback;

    public AIReview(boolean isCorrect, int score, String feedback) {
        this.isCorrect = isCorrect;
        this.score = score;
        this.feedback = feedback;
    }
}