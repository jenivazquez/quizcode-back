package com.quizcode.module.question.application;

import com.quizcode.module.question.application.validation.QuestionValidator;
import com.quizcode.module.question.domain.QuestionRepository;
import com.quizcode.module.question.domain.QuestionService;
import com.quizcode.module.question.domain.entity.question.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionValidator questionValidator;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionValidator questionValidator) {
        this.questionRepository = questionRepository;
        this.questionValidator = questionValidator;
    }

    @Override
    public String create(String ownerId, Question question) {
        questionValidator.validateToCreate(ownerId, question);
        return questionRepository.create(question);
    }

    @Override
    public List<Question> findByQuizId(String ownerId, String quizId) {
        questionValidator.validateToFindByQuizId(ownerId, quizId);
        return questionRepository.findByQuizId(quizId);
    }

    @Override
    public void update(String ownerId, Question question) {
        questionValidator.validateToUpdate(ownerId, question);
        questionRepository.update(question);
    }

    @Override
    public void delete(String ownerId, String quizId, String id) {
        questionValidator.validateToDelete(ownerId, quizId, id);
        questionRepository.delete(id);
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
