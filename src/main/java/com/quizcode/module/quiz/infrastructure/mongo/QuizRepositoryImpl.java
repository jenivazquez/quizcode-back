package com.quizcode.module.quiz.infrastructure.mongo;

import com.quizcode.module.quiz.domain.QuizRepository;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;
import com.quizcode.module.quiz.infrastructure.mongo.mapper.QuizMongoMapper;
import com.quizcode.module.quiz.infrastructure.mongo.persistence.QuizMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class QuizRepositoryImpl implements QuizRepository {

    private final QuizMongoRepository quizMongoRepository;
    private final QuizMongoMapper quizMongoMapper;

    public QuizRepositoryImpl(QuizMongoRepository quizMongoRepository, QuizMongoMapper quizMongoMapper) {
        this.quizMongoRepository = quizMongoRepository;
        this.quizMongoMapper = quizMongoMapper;
    }

    @Override
    public String create(Quiz quiz) {
        return quizMongoRepository.insert(quizMongoMapper.quizToQuizDocument(quiz)).getId();
    }

    @Override
    public Optional<Quiz> findById(String id) {
        return quizMongoRepository.findById(id).map(quizMongoMapper::quizDocumentToQuiz);
    }

    @Override
    public List<Quiz> findByOwnerId(String ownerId) {
        return quizMongoRepository.findByOwnerId(ownerId).stream()
                .map(quizMongoMapper::quizDocumentToQuiz)
                .toList();
    }

    @Override
    public boolean existsByOwnerIdAndTitle(String ownerId, String title) {
        return quizMongoRepository.existsByOwnerIdAndTitle(ownerId, title);
    }

    @Override
    public boolean existsByOwnerIdAndTitleExcludingId(String ownerId, String title, String id) {
        return quizMongoRepository.existsByOwnerIdAndTitleAndIdNot(ownerId, title, id);
    }

    @Override
    public void update(Quiz quiz) {
        quizMongoRepository.update(quiz.getId(), quiz.getTitle(), quiz.getDescription(),
                                    quiz.getHasLimit(), quiz.getLimitMinutes(), quiz.getUpdatedAt());
    }

    @Override
    public void updateStatus(String id, QuizStatus status) {
        quizMongoRepository.updateStatus(id, status);
    }

    @Override
    public void delete(String id) {
        quizMongoRepository.deleteById(id);
    }

}