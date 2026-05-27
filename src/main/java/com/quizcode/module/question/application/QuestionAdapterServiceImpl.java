package com.quizcode.module.question.application;

import com.quizcode.module.question.domain.QuestionAdapterService;
import com.quizcode.module.question.domain.QuestionRepository;
import com.quizcode.module.question.domain.entity.question.Question;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Question> findByQuizId(String quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    @Override
    public boolean existsByQuizIdAndId(String quizId, String questionId) {
        return questionRepository.existsByQuizIdAndId(quizId, questionId);
    }
}