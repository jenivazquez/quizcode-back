package com.quizcode.module.quiz.domain;

import com.quizcode.module.quiz.domain.entity.Quiz;

import java.util.List;

public interface QuizAdapterService {
    Quiz findById(String id, String ownerId);
    List<Quiz> findByOwnerId(String ownerId);
    void lockQuizIfHasRooms(String id);
    void unlockQuizIfNoRooms(String id);
}