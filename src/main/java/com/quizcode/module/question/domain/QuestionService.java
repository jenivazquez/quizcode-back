package com.quizcode.module.question.domain;

import com.quizcode.module.question.domain.entity.message.AIMessage;
import com.quizcode.module.question.domain.entity.question.AIQuestion;
import com.quizcode.module.question.domain.entity.question.Question;

import java.util.List;

public interface QuestionService {
    String create(String ownerId, Question question);
    List<Question> findByQuizId(String ownerId, String quizId);
    List<Question> findByQuizIdToAnswer(String quizId, String participationId);
    List<Question> findByQuizIdToReview(String quizId, String participationId);
    void update(String ownerId, Question question);
    void delete(String ownerId, String quizId, String id);
    AIQuestion generateAIQuestion(String ownerId, String quizId, List<AIMessage> messages);
}
