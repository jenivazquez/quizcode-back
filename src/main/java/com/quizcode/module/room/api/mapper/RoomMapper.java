package com.quizcode.module.room.api.mapper;

import com.quizcode.module.room.api.dto.CreateRoomRequest;
import com.quizcode.module.room.api.dto.IdRoomResponse;
import com.quizcode.module.room.api.dto.RoomResponse;
import com.quizcode.module.room.api.dto.UpdateRoomRequest;
import com.quizcode.module.room.domain.entity.EditRoom;
import com.quizcode.module.room.domain.entity.NewRoom;
import com.quizcode.module.room.domain.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    default Room createRoomRequestToRoom(String quizId, CreateRoomRequest request) {
        return new NewRoom(request.getName(), request.getDescription(), quizId).getRoom();
    }

    default Room updateRoomRequestToRoom(String id, String quizId, UpdateRoomRequest request) {
        return new EditRoom(id, request.getName(), request.getDescription(), quizId).getRoom();
    }

    RoomResponse roomToRoomResponse(Room room);

    IdRoomResponse roomToIdRoomResponse(Room room);
}
