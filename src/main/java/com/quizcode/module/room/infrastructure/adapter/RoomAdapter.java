package com.quizcode.module.room.infrastructure.adapter;

import com.quizcode.module.quiz.domain.QuizToRoomPort;
import com.quizcode.module.room.domain.RoomAdapterService;
import org.springframework.stereotype.Component;

@Component
public class RoomAdapter implements QuizToRoomPort {

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
}