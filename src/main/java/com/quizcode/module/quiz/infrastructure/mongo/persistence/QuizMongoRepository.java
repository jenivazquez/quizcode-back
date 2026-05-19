package com.quizcode.module.quiz.infrastructure.mongo.persistence;

import com.quizcode.module.quiz.domain.entity.QuizStatus;
import com.quizcode.module.quiz.infrastructure.mongo.document.QuizDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.Instant;
import java.util.List;

public interface QuizMongoRepository extends MongoRepository<QuizDocument, String> {

    List<QuizDocument> findByOwnerId(String ownerId);

    boolean existsByOwnerIdAndTitle(String ownerId, String title);

    boolean existsByOwnerIdAndTitleAndIdNot(String ownerId, String title, String id);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'title': ?1, 'description': ?2, 'hasLimit': ?3, 'limitMinutes': ?4, 'updatedAt': ?5 } }")
    void update(String id, String title, String description, Boolean hasLimit, Integer limitMinutes, Instant updatedAt);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'status': ?1 } }")
    void updateStatus(String id, QuizStatus status);
}
