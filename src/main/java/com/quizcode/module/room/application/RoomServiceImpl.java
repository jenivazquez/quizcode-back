package com.quizcode.module.room.application;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.room.application.validation.RoomValidator;
import com.quizcode.module.room.domain.RoomToQuizPort;
import com.quizcode.module.room.domain.RoomRepository;
import com.quizcode.module.room.domain.RoomService;
import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.RoomStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class RoomServiceImpl implements RoomService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RoomRepository roomRepository;
    private final RoomValidator roomValidator;
    private final RoomToQuizPort quizPort;

    public RoomServiceImpl(RoomRepository roomRepository, RoomValidator roomValidator, RoomToQuizPort quizPort) {
        this.roomRepository = roomRepository;
        this.roomValidator = roomValidator;
        this.quizPort = quizPort;
    }

    @Override
    public String create(String ownerId, Room room) {
        roomValidator.validateToCreate(ownerId, room);
        String id = roomRepository.create(room);
        quizPort.lockQuizIfHasRooms(room.getQuizId());
        return id;
    }

    @Override
    public Room findById(String id, String ownerId, String quizId) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("La sala no existe"));
        roomValidator.validateToFindById(room, ownerId, quizId);
        return room;
    }

    @Override
    public List<Room> findByQuizId(String ownerId, String quizId) {
        roomValidator.validateToFindByQuizId(ownerId, quizId);
        return roomRepository.findByQuizId(quizId);
    }

    @Override
    public List<Room> findByOwnerId(String ownerId) {
        List<String> quizIds = quizPort.findQuizzesByOwner(ownerId);
        return (quizIds.isEmpty()) ? List.of() : roomRepository.findByQuizIds(quizIds);
    }

    @Override
    public Room findByCode(String code) {
        return roomRepository.findByCode(code).orElseThrow(() -> new NotFoundExceptionCustom("No existe ninguna sala con ese código"));
    }

    @Override
    public void update(String ownerId, Room room) {
        roomValidator.validateToUpdate(ownerId, room);
        roomRepository.update(room);
    }

    @Override
    public void delete(String id, String ownerId, String quizId) {
        roomValidator.validateToDelete(id, ownerId, quizId);
        roomRepository.delete(id);
        quizPort.unlockQuizIfNoRooms(quizId);
    }

    @Override
    public void updateStatus(String id, String ownerId, String quizId, RoomStatus status) {
        Room savedRoom = roomRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("La sala no existe"));
        roomValidator.validateToUpdateStatus(savedRoom, ownerId, quizId, status);
        switch (status) {
            case OPENED -> openRoom(id, savedRoom);
            case PAUSED -> roomRepository.pauseRoom(id);
            case CLOSED -> roomRepository.closeRoom(id, Instant.now());
        }
    }

    private void openRoom(String id, Room savedRoom) {
        if (savedRoom.getStatus() == RoomStatus.CREATED) {
            roomRepository.openRoom(id, generateUniqueCode(), Instant.now());
        } else {
            roomRepository.reopenRoom(id);
        }
    }

    private String generateUniqueCode() {
        return Stream.generate(this::generateCode)
                .filter(code -> !roomRepository.existsByCode(code))
                .findAny()
                .orElseThrow(() -> new AutoGenerationExceptionCustom("Error al generar el código único de la sala"));
    }

    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(chars.charAt(SECURE_RANDOM.nextInt(chars.length()))))
                .collect(Collectors.joining());
    }
}
