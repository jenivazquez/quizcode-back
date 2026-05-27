package com.quizcode.module.room.infrastructure.adapter;

import com.quizcode.module.participation.domain.ParticipationToRoomPort;
import com.quizcode.module.quiz.domain.QuizToRoomPort;
import com.quizcode.module.room.domain.RoomAdapterService;
import org.springframework.stereotype.Component;

@Component
public class RoomAdapter implements QuizToRoomPort, ParticipationToRoomPort {

    private final RoomAdapterService roomAdapterService;

    public RoomAdapter(RoomAdapterService roomAdapterService) {
        this.roomAdapterService = roomAdapterService;
    }

    @Override
    public boolean hasRooms(String quizId) {
        return roomAdapterService.hasRooms(quizId);
    }

    @Override
    public void deleteRoomsByQuizId(String quizId) {
        roomAdapterService.deleteRoomsByQuizId(quizId);
    }

    @Override
    public boolean roomExists(String roomId) {
        return roomAdapterService.roomExists(roomId);
    }

    @Override
    public boolean isRoomOpened(String roomId) {
        return roomAdapterService.isRoomOpened(roomId);
    }

    @Override
    public boolean isRoomCreated(String roomId) {
        return roomAdapterService.isRoomCreated(roomId);
    }

    @Override
    public boolean isRoomClosed(String roomId) {
        return roomAdapterService.isRoomClosed(roomId);
    }

    @Override
    public boolean isRoomReviewed(String roomId) {
        return roomAdapterService.isRoomReviewed(roomId);
    }

    @Override
    public String getQuizIdByRoom(String roomId) {
        return roomAdapterService.getQuizIdByRoom(roomId);
    }

    @Override
    public boolean roomBelongsToQuiz(String roomId, String quizId) {
        return roomAdapterService.roomBelongsToQuiz(roomId, quizId);
    }
}
