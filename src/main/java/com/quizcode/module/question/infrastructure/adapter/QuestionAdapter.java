package com.quizcode.module.question.infrastructure.adapter;

import com.quizcode.module.question.domain.QuestionAdapterService;
import com.quizcode.module.quiz.domain.QuizToQuestionPort;
import org.springframework.stereotype.Component;

@Component
public class QuestionAdapter implements QuizToQuestionPort {

    private final QuestionAdapterService questionAdapterService;

    public QuestionAdapter(QuestionAdapterService questionAdapterService) {
        this.questionAdapterService = questionAdapterService;
    }

    @Override
    public void deleteQuestionsByQuizId(String quizId) {
        questionAdapterService.deleteByQuizId(quizId);
    }

    @Override
    public boolean hasQuestions(String quizId) {
        return questionAdapterService.hasQuestions(quizId);
    }
}