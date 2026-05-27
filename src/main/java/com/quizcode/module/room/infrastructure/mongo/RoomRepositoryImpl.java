package com.quizcode.module.room.infrastructure.mongo;

import com.quizcode.module.room.domain.RoomRepository;
import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.RoomStatus;
import com.quizcode.module.room.infrastructure.mongo.mapper.RoomMongoMapper;
import com.quizcode.module.room.infrastructure.mongo.persistence.RoomMongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomMongoRepository roomMongoRepository;
    private final RoomMongoMapper roomMongoMapper;

    public RoomRepositoryImpl(RoomMongoRepository roomMongoRepository, RoomMongoMapper roomMongoMapper) {
        this.roomMongoRepository = roomMongoRepository;
        this.roomMongoMapper = roomMongoMapper;
    }

    @Override
    public String create(Room room) {
        return roomMongoRepository.insert(roomMongoMapper.roomToRoomDocument(room)).getId();
    }

    @Override
    public Optional<Room> findById(String id) {
        return roomMongoRepository.findById(id).map(roomMongoMapper::roomDocumentToRoom);
    }

    @Override
    public List<Room> findByQuizId(String quizId) {
        return roomMongoRepository.findByQuizId(quizId).stream()
                .map(roomMongoMapper::roomDocumentToRoom)
                .toList();
    }

    @Override
    public List<Room> findByQuizIds(List<String> quizIds) {
        return roomMongoRepository.findByQuizIdIn(quizIds).stream()
                .map(roomMongoMapper::roomDocumentToRoom)
                .toList();
    }

    @Override
    public Optional<Room> findByCode(String code) {
        return roomMongoRepository.findByCode(code).map(roomMongoMapper::roomDocumentToRoom);
    }

    @Override
    public void update(Room room) {
        roomMongoRepository.update(room.getId(), room.getName(), room.getDescription());
    }

    @Override
    public void openRoom(String id, String code, Instant startedAt) {
        roomMongoRepository.openRoom(id, RoomStatus.OPENED, code, startedAt);
    }

    @Override
    public void pauseRoom(String id) {
        roomMongoRepository.updateStatus(id, RoomStatus.PAUSED);
    }

    @Override
    public void reopenRoom(String id) {
        roomMongoRepository.updateStatus(id, RoomStatus.OPENED);
    }

    @Override
    public void closeRoom(String id, Instant finishedAt) {
        roomMongoRepository.closeRoom(id, RoomStatus.CLOSED, null, finishedAt);
    }

    @Override
    public void delete(String id) {
        roomMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByQuizIdAndName(String quizId, String name) {
        return roomMongoRepository.existsByQuizIdAndName(quizId, name);
    }

    @Override
    public boolean existsByQuizIdAndNameAndIdNot(String quizId, String name, String id) {
        return roomMongoRepository.existsByQuizIdAndNameAndIdNot(quizId, name, id);
    }

    @Override
    public boolean existsByCode(String code) {
        return roomMongoRepository.existsByCode(code);
    }

    @Override
    public boolean existsByQuizId(String quizId) {
        return roomMongoRepository.existsByQuizId(quizId);
    }

    @Override
    public boolean existsByQuizIdAndStatus(String quizId, RoomStatus status) {
        return roomMongoRepository.existsByQuizIdAndStatus(quizId, status);
    }

    @Override
    public void deleteByQuizId(String quizId) {
        roomMongoRepository.deleteByQuizId(quizId);
    }

    @Override
    public void markAsReviewed(String id) {
        roomMongoRepository.markAsReviewed(id);
    }
}
