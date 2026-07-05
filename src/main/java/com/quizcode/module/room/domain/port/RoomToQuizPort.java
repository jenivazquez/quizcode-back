package com.quizcode.module.room.domain.port;

import java.util.Map;

public interface RoomToQuizPort {
    void checkQuizExistByOwner(String quizId, String ownerId);
    boolean isRoomAllowedByQuiz(String quizId, String ownerId);
    String findQuizTitleById(String quizId);
    Map<String, String> findQuizTitlesByOwner(String ownerId);
    void lockQuizIfHasRooms(String quizId);
    void unlockQuizIfNoRooms(String quizId);
}