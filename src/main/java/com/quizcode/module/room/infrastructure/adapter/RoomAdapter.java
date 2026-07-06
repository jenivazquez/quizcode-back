package com.quizcode.module.room.infrastructure.adapter;

import com.quizcode.module.participation.domain.port.ParticipationToRoomPort;
import com.quizcode.module.quiz.domain.port.QuizToRoomPort;
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
    public void deleteRoomsAndPartsByQuizId(String quizId) {
        roomAdapterService.deleteRoomsAndPartsByQuizId(quizId);
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
