package com.quizcode.module.room.domain;

import java.util.List;

public interface RoomToQuizPort {
    void checkQuizExistByOwner(String quizId, String ownerId);
    boolean isRoomAllowedByQuiz(String quizId, String ownerId);
    List<String> findQuizzesByOwner(String ownerId);
    void lockQuizIfHasRooms(String quizId);
    void unlockQuizIfNoRooms(String quizId);
}