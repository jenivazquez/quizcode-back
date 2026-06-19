package com.quizcode.module.question.domain;

public interface QuestionToParticipationPort {
    boolean isParticipationStarted(String participationId);
    boolean isParticipationFinished(String participationId);
}
