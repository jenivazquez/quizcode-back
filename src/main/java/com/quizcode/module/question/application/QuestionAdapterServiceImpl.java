package com.quizcode.module.question.application;

import com.quizcode.module.question.domain.QuestionAdapterService;
import com.quizcode.module.question.domain.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionAdapterServiceImpl implements QuestionAdapterService {

    private final QuestionRepository questionRepository;

    public QuestionAdapterServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void deleteByQuizId(String quizId) {
        questionRepository.deleteByQuizId(quizId);
    }

    @Override
    public boolean hasQuestions(String quizId) {
        return questionRepository.existsByQuizId(quizId);
    }
}