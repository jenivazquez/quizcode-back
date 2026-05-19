package com.quizcode.module.question.infrastructure.mongo;

import com.quizcode.module.question.domain.QuestionRepository;
import com.quizcode.module.question.domain.entity.question.Question;
import com.quizcode.module.question.infrastructure.mongo.mapper.QuestionMongoMapper;
import com.quizcode.module.question.infrastructure.mongo.persistence.QuestionMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionMongoRepository questionMongoRepository;
    private final QuestionMongoMapper questionMongoMapper;

    public QuestionRepositoryImpl(QuestionMongoRepository questionMongoRepository, QuestionMongoMapper questionMongoMapper) {
        this.questionMongoRepository = questionMongoRepository;
        this.questionMongoMapper = questionMongoMapper;
    }

    @Override
    public String create(Question question) {
        return questionMongoRepository.insert(questionMongoMapper.questionToQuestionDocument(question)).getId();
    }

    @Override
    public List<Question> findByQuizId(String quizId) {
        return questionMongoRepository.findByQuizId(quizId).stream()
                .map(questionMongoMapper::questionDocumentToQuestion)
                .toList();
    }

    @Override
    public boolean existsByQuizIdAndId(String quizId, String id) {
        return questionMongoRepository.existsByQuizIdAndId(quizId, id);
    }

    @Override
    public boolean existsByQuizIdAndOrder(String quizId, Integer order) {
        return questionMongoRepository.existsByQuizIdAndOrder(quizId, order);
    }

    @Override
    public boolean existsByQuizIdAndOrderAndIdNot(String quizId, Integer order, String id) {
        return questionMongoRepository.existsByQuizIdAndOrderAndIdNot(quizId, order, id);
    }

    @Override
    public void update(Question question) {
        questionMongoRepository.save(questionMongoMapper.questionToQuestionDocument(question));
    }

    @Override
    public void delete(String id) {
        questionMongoRepository.deleteById(id);
    }

    @Override
    public void deleteByQuizId(String quizId) {
        questionMongoRepository.deleteByQuizId(quizId);
    }
}
