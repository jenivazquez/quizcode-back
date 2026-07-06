package com.quizcode.module.participation.infrastructure.mongo.persistence;

import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.module.participation.infrastructure.mongo.document.AnswerDocument;
import com.quizcode.module.participation.infrastructure.mongo.document.ParticipationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ParticipationMongoRepository extends MongoRepository<ParticipationDocument, String> {

    Optional<ParticipationDocument> findByRoomIdAndUsername(String roomId, String username);

    List<ParticipationDocument> findByRoomId(String roomId);

    List<ParticipationDocument> findByRoomIdAndStatus(String roomId, ParticipationStatus status);

    List<ParticipationDocument> findByRoomIdAndReviewStatusIn(String roomId, List<ReviewStatus> statuses);

    boolean existsByRoomIdAndUsername(String roomId, String username);

    boolean existsByRoomIdAndStatus(String roomId, ParticipationStatus status);

    boolean existsByRoomIdAndReviewStatus(String roomId, ReviewStatus reviewStatus);

    boolean existsByRoomIdAndReviewStatusIn(String roomId, List<ReviewStatus> listStatus);

    void deleteByRoomId(String roomId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'answers': ?1, 'finishedAt': ?2, 'status': ?3, 'reviewStatus': ?4, 'totalTime': ?5, 'totalScore': ?6 } }")
    void update(String id, List<AnswerDocument> answers, Instant finishedAt, ParticipationStatus status, ReviewStatus reviewStatus, Long totalTime, Integer totalScore);
}
