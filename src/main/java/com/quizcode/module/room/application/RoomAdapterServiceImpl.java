package com.quizcode.module.room.application;

import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.room.application.validation.RoomValidator;
import com.quizcode.module.room.domain.RoomAdapterService;
import com.quizcode.module.room.domain.RoomRepository;
import com.quizcode.module.room.domain.entity.Room;
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

    @Override
    public boolean roomExists(String roomId) {
        return roomRepository.findById(roomId).isPresent();
    }

    @Override
    public boolean isRoomOpened(String roomId) {
        return findOrThrow(roomId).getStatus() == RoomStatus.OPENED;
    }

    @Override
    public boolean isRoomCreated(String roomId) {
        return findOrThrow(roomId).getStatus() == RoomStatus.CREATED;
    }

    @Override
    public boolean isRoomClosed(String roomId) {
        return findOrThrow(roomId).getStatus() == RoomStatus.CLOSED;
    }

    @Override
    public boolean isRoomReviewed(String roomId) {
        return Boolean.TRUE.equals(findOrThrow(roomId).getReviewed());
    }

    @Override
    public String getQuizIdByRoom(String roomId) {
        return findOrThrow(roomId).getQuizId();
    }

    @Override
    public boolean roomBelongsToQuiz(String roomId, String quizId) {
        return findOrThrow(roomId).getQuizId().equals(quizId);
    }

    private Room findOrThrow(String roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new NotFoundExceptionCustom("La sala no existe"));
    }
}