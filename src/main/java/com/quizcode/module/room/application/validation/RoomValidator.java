package com.quizcode.module.room.application.validation;

import com.quizcode.error.exception.ForbiddenAccessExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.room.domain.RoomToQuizPort;
import com.quizcode.module.room.domain.RoomRepository;
import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.RoomStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    private final RoomRepository roomRepository;
    private final RoomToQuizPort quizPort;

    public RoomValidator(RoomRepository roomRepository, @Lazy RoomToQuizPort quizPort) {
        this.roomRepository = roomRepository;
        this.quizPort = quizPort;
    }

    public void validateToCreate(String ownerId, Room newRoom) {
        checkQuizAcceptsRooms(newRoom.getQuizId(), ownerId);
        checkNewNameUnique(newRoom.getQuizId(), newRoom.getName());
    }

    public void validateToUpdate(String ownerId, Room newRoom) {
        Room savedRoom = roomRepository.findById(newRoom.getId()).orElseThrow(() -> new NotFoundExceptionCustom("La sala no existe"));
        checkQuizAcceptsRooms(newRoom.getQuizId(), ownerId);
        checkRoomBelongsToQuiz(savedRoom, newRoom.getQuizId());
        checkRoomEditable(savedRoom);
        checkEditNameUnique(newRoom.getQuizId(), newRoom.getName(), newRoom.getId());
    }

    public void validateToFindById(Room savedRoom, String ownerId, String quizId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongsToQuiz(savedRoom, quizId);
    }

    public void validateToFindByQuizId(String ownerId, String quizId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
    }

    public void validateToUpdateStatus(Room savedRoom, String ownerId, String quizId, RoomStatus status) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongsToQuiz(savedRoom, quizId);
        checkStatusTransition(savedRoom.getStatus(), status);
    }

    public void validateToDelete(String id, String ownerId, String quizId) {
        Room savedRoom = roomRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("La sala no existe"));
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongsToQuiz(savedRoom, quizId);
        checkRoomDeletable(savedRoom);
    }

    public void validateToDeleteByQuizId(String quizId) {
        checkExistRoomsOpenedByQuizId(quizId);
    }

    private void checkStatusTransition(RoomStatus savedStatus, RoomStatus sentStatus) {
        if (sentStatus == null) {
            throw new InvalidDataExceptionCustom("El campo estado es obligatorio");
        }
        if (sentStatus == RoomStatus.CREATED) {
            throw new InvalidDataExceptionCustom("No puedes cambiar el estado a borrador manualmente");
        }
        if (savedStatus == RoomStatus.CLOSED) {
            throw new InvalidStatusExceptionCustom("La sala ya está cerrada y no puede cambiar de estado");
        }
        if (savedStatus == sentStatus) {
            throw new InvalidStatusExceptionCustom("La sala ya está en ese estado");
        }
        if (savedStatus == RoomStatus.CREATED && sentStatus != RoomStatus.OPENED) {
            throw new InvalidStatusExceptionCustom("Una sala en estado borrador solo puede cambiar al estado abierto");
        }
        if (savedStatus == RoomStatus.OPENED && sentStatus != RoomStatus.PAUSED) {
            throw new InvalidStatusExceptionCustom("Una sala abierta solo puede cambiar al estado en pausa");
        }
    }

    private void checkRoomEditable(Room room) {
        if (!room.isEditable()) {
            throw new InvalidStatusExceptionCustom("No puedes modificar una sala que ya está abierta");
        }
    }

    private void checkRoomDeletable(Room room) {
        if (room.getStatus() == RoomStatus.OPENED) {
            throw new InvalidStatusExceptionCustom("No puedes eliminar una sala abierta");
        }
    }

    private void checkRoomBelongsToQuiz(Room room, String quizId) {
        if (!room.getQuizId().equals(quizId)) {
            throw new ForbiddenAccessExceptionCustom("La sala no pertenece a este cuestionario");
        }
    }

    private void checkNewNameUnique(String quizId, String name) {
        if (roomRepository.existsByQuizIdAndName(quizId, name)) {
            throw new InvalidDataExceptionCustom("Este cuestionario ya tiene una sala con ese nombre");
        }
    }

    private void checkEditNameUnique(String quizId, String name, String id) {
        if (roomRepository.existsByQuizIdAndNameAndIdNot(quizId, name, id)) {
            throw new InvalidDataExceptionCustom("Este cuestionario ya tiene una sala con ese nombre");
        }
    }

    private void checkQuizAcceptsRooms(String quizId, String ownerId) {
        if (!quizPort.isRoomAllowedByQuiz(quizId, ownerId)) {
            throw new InvalidStatusExceptionCustom("El estado del cuestionario no permite añadir o modificar salas");
        }
    }

    private void checkExistRoomsOpenedByQuizId(String quizId) {
        if (roomRepository.existsByQuizIdAndStatus(quizId, RoomStatus.OPENED)) {
            throw new InvalidStatusExceptionCustom("No se pueden eliminar las salas del cuestionario porque al menos una de ellas está en estado abierta");
        }
    }


}
