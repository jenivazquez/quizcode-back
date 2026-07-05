package com.quizcode.module.participation.domain.port;

import com.quizcode.module.participation.domain.entity.ai.AIReview;

public interface AIReviewPort {

    AIReview reviewCode(String statement, String baseCode, String writtenAnswer, int maxScore);
}