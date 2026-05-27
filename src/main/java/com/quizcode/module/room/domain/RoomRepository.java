package com.quizcode.module.room.domain;

import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.RoomStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    String create(Room room);
    Optional<Room> findById(String id);
    void markAsReviewed(String id);
    List<Room> findByQuizId(String quizId);
    List<Room> findByQuizIds(List<String> quizIds);
    Optional<Room> findByCode(String code);
    void update(Room room);
    void openRoom(String id, String code, Instant startedAt);
    void pauseRoom(String id);
    void reopenRoom(String id);
    void closeRoom(String id, Instant finishedAt);
    void delete(String id);
    void deleteByQuizId(String quizId);
    boolean existsByQuizIdAndName(String quizId, String name);
    boolean existsByQuizIdAndNameAndIdNot(String quizId, String name, String id);
    boolean existsByCode(String code);
    boolean existsByQuizId(String quizId);
    boolean existsByQuizIdAndStatus(String quizId, RoomStatus status);
}
