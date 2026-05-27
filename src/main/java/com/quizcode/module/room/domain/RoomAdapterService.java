package com.quizcode.module.room.domain;

public interface RoomAdapterService {
    boolean hasRooms(String quizId);
    void deleteRoomsByQuizId(String quizId);
    boolean roomExists(String roomId);
    boolean isRoomOpened(String roomId);
    boolean isRoomCreated(String roomId);
    boolean isRoomClosed(String roomId);
    boolean isRoomReviewed(String roomId);
    String getQuizIdByRoom(String roomId);
    boolean roomBelongsToQuiz(String roomId, String quizId);
}