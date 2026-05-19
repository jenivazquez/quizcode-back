package com.quizcode.module.quiz.infrastructure.adapter;

import com.quizcode.module.question.domain.QuizPort;
import com.quizcode.module.quiz.domain.QuizService;
import org.springframework.stereotype.Component;

@Component
public class QuizAdapter implements QuizPort {

    private final QuizService quizService;

    public QuizAdapter(QuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void checkQuizAccess(String quizId, String ownerId) {
        quizService.findById(quizId, ownerId);
    }

    @Override
    public boolean isQuizEditable(String quizId, String ownerId) {
        return quizService.findById(quizId, ownerId).isEditable();
    }
}
