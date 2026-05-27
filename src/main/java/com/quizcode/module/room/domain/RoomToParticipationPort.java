package com.quizcode.module.room.domain;

public interface RoomToParticipationPort {
    boolean hasStartedParticipations(String roomId);
    boolean hasPendingReviews(String roomId);
}
