package com.quizcode.module.question.infrastructure.adapter;

import com.quizcode.module.question.domain.QuestionService;
import com.quizcode.module.quiz.domain.QuestionPort;
import org.springframework.stereotype.Component;

@Component
public class QuestionAdapter implements QuestionPort {

    private final QuestionService questionService;

    public QuestionAdapter(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public void deleteQuestionsByQuizId(String quizId) {
        questionService.deleteByQuizId(quizId);
    }
}
