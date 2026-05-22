package com.quizcode.module.room.domain;

public interface RoomAdapterService {
    boolean hasRooms(String quizId);
    void deleteRoomsByQuizId(String quizId);
}