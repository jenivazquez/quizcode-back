package com.quizcode.module.question.domain.port;

public interface QuestionToParticipationPort {
    boolean isParticipationStarted(String participationId);
    boolean isParticipationFinished(String participationId);
}
