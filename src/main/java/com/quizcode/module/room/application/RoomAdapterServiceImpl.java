package com.quizcode.module.room.application;

import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.module.room.application.validation.RoomValidator;
import com.quizcode.module.room.domain.RoomAdapterService;
import com.quizcode.module.room.domain.RoomRepository;
import com.quizcode.module.room.domain.entity.RoomStatus;
import org.springframework.stereotype.Service;

@Service
public class RoomAdapterServiceImpl implements RoomAdapterService {

    private final RoomRepository roomRepository;
    private final RoomValidator roomValidator;

    public RoomAdapterServiceImpl(RoomRepository roomRepository, RoomValidator roomValidator) {
        this.roomRepository = roomRepository;
        this.roomValidator = roomValidator;
    }

    @Override
    public boolean hasRooms(String quizId) {
        return roomRepository.existsByQuizId(quizId);
    }

    @Override
    public void deleteRoomsByQuizId(String quizId) {
        roomValidator.validateToDeleteByQuizId(quizId);
        roomRepository.deleteByQuizId(quizId);
    }
}