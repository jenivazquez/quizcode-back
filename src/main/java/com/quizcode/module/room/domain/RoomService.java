package com.quizcode.module.room.domain;

import com.quizcode.module.room.domain.entity.QuizRoom;
import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.RoomStatus;

import java.util.List;

public interface RoomService {
    String create(String ownerId, Room room);
    QuizRoom findById(String id, String ownerId, String quizId);
    List<QuizRoom> findByQuizId(String ownerId, String quizId);
    List<QuizRoom> findByOwnerId(String ownerId);
    Room findByCode(String code);
    Room findByIdAsParticipant(String id);
    void update(String ownerId, Room room);
    void updateStatus(String id, String ownerId, String quizId, RoomStatus status);
    void markAsReviewed(String id, String ownerId, String quizId);
    void delete(String id, String ownerId, String quizId);
}
