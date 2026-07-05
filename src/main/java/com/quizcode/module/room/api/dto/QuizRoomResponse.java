package com.quizcode.module.room.api.dto;

import com.quizcode.module.room.domain.entity.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizRoomResponse {
    private String id;
    private String name;
    private String description;
    private String code;
    private RoomStatus status;
    private String quizId;
    private String quizTitle;
    private Instant createdAt;
    private Instant startedAt;
    private Instant finishedAt;
    private Boolean reviewed;
}
