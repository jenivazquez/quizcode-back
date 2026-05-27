package com.quizcode.module.participation.domain;

public interface ParticipationToRoomPort {
    boolean roomExists(String roomId);
    boolean isRoomOpened(String roomId);
    boolean isRoomCreated(String roomId);
    boolean isRoomClosed(String roomId);
    boolean isRoomReviewed(String roomId);
    String getQuizIdByRoom(String roomId);
    boolean roomBelongsToQuiz(String roomId, String quizId);
}