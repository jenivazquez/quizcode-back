package com.quizcode.module.participation.domain;

public interface ParticipationAdapterService {
    boolean hasStartedParticipations(String roomId);
    boolean hasPendingReviews(String roomId);
    boolean isParticipationStarted(String participationId);
    boolean isParticipationFinished(String participationId);
}
