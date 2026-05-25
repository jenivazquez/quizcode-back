package com.quizcode.module.quiz.domain;

import com.quizcode.module.quiz.domain.entity.Quiz;

import java.util.List;

public interface QuizAdapterService {
    Quiz findByIdAndOwnerId(String id, String ownerId);
    Quiz findById(String id);
    List<Quiz> findByOwnerId(String ownerId);
    void lockQuizIfHasRooms(String id);
    void unlockQuizIfNoRooms(String id);
}