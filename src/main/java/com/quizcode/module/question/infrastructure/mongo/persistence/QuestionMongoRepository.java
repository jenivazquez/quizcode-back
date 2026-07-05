package com.quizcode.module.question.infrastructure.mongo.persistence;

import com.quizcode.module.question.infrastructure.mongo.document.QuestionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionMongoRepository extends MongoRepository<QuestionDocument, String> {

    @Query(value = "{ 'quizId': ?0 }", sort = "{ 'order': 1, 'statement': 1 }")
    List<QuestionDocument> findByQuizId(String quizId);

    void deleteByQuizId(String quizId);

    boolean existsByQuizIdAndId(String quizId, String id);

    boolean existsByQuizIdAndOrder(String quizId, Integer order);

    boolean existsByQuizIdAndOrderAndIdNot(String quizId, Integer order, String id);

    boolean existsByQuizId(String quizId);
}
