package com.quizcode.module.room.application;

import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.room.application.validation.RoomValidator;
import com.quizcode.module.room.domain.RoomAdapterService;
import com.quizcode.module.room.domain.RoomRepository;
import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.RoomStatus;
import com.quizcode.module.room.domain.port.RoomToParticipationPort;
import org.springframework.stereotype.Service;

@Service
public class RoomAdapterServiceImpl implements RoomAdapterService {

    private final RoomRepository roomRepository;
    private final RoomValidator roomValidator;
    private final RoomToParticipationPort participationPort;

    public RoomAdapterServiceImpl(RoomRepository roomRepository, RoomValidator roomValidator, RoomToParticipationPort participationPort) {
        this.roomRepository = roomRepository;
        this.roomValidator = roomValidator;
        this.participationPort = participationPort;
    }

    @Override
    public boolean hasRooms(String quizId) {
        return roomRepository.existsByQuizId(quizId);
    }

    @Override
    public void deleteRoomsAndPartsByQuizId(String quizId) {
        roomValidator.validateToDeleteByQuizId(quizId);
        roomRepository.findByQuizId(quizId).forEach(room -> participationPort.deleteParticipationsByRoomId(room.getId()));
        roomRepository.deleteByQuizId(quizId);
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