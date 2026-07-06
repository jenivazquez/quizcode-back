package com.quizcode.module.room.domain.port;

public interface RoomToParticipationPort {
    boolean hasStartedParticipations(String roomId);
    boolean hasPendingReviews(String roomId);
    void deleteParticipationsByRoomId(String roomId);
}
