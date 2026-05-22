package com.quizcode.module.room.api.dto;

import com.quizcode.module.room.domain.entity.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomStatusRequest {
    private RoomStatus status;
}
