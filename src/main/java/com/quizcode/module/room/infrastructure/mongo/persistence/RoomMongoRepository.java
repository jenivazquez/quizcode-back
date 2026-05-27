package com.quizcode.module.room.infrastructure.mongo.persistence;

import com.quizcode.module.room.domain.entity.RoomStatus;
import com.quizcode.module.room.infrastructure.mongo.document.RoomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RoomMongoRepository extends MongoRepository<RoomDocument, String> {

    List<RoomDocument> findByQuizId(String quizId);

    List<RoomDocument> findByQuizIdIn(List<String> quizIds);

    Optional<RoomDocument> findByCode(String code);

    boolean existsByQuizIdAndName(String quizId, String name);

    boolean existsByQuizIdAndNameAndIdNot(String quizId, String name, String id);

    boolean existsByCode(String code);

    boolean existsByQuizId(String quizId);
    boolean existsByQuizIdAndStatus(String quizId, RoomStatus status);
    void deleteByQuizId(String quizId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'name': ?1, 'description': ?2 } }")
    void update(String id, String name, String description);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'status': ?1, 'code': ?2, 'startedAt': ?3 } }")
    void openRoom(String id, RoomStatus status, String code, Instant startedAt);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'status': ?1 } }")
    void updateStatus(String id, RoomStatus status);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'status': ?1, 'code': ?2, 'finishedAt': ?3 } }")
    void closeRoom(String id, RoomStatus status, String code, Instant finishedAt);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'reviewed': true } }")
    void markAsReviewed(String id);
}
