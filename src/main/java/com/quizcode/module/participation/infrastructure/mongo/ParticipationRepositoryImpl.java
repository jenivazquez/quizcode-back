package com.quizcode.module.participation.infrastructure.mongo;

import com.quizcode.module.participation.domain.ParticipationRepository;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.module.participation.infrastructure.mongo.document.AnswerDocument;
import com.quizcode.module.participation.infrastructure.mongo.document.ParticipationDocument;
import com.quizcode.module.participation.infrastructure.mongo.mapper.ParticipationMongoMapper;
import com.quizcode.module.participation.infrastructure.mongo.persistence.ParticipationMongoRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Repository
public class ParticipationRepositoryImpl implements ParticipationRepository {

    private final ParticipationMongoRepository partMongoRepository;
    private final ParticipationMongoMapper partMongoMapper;
    private final MongoTemplate mongoTemplate;

    public ParticipationRepositoryImpl(ParticipationMongoRepository partMongoRepository, ParticipationMongoMapper partMongoMapper, MongoTemplate mongoTemplate) {
        this.partMongoRepository = partMongoRepository;
        this.partMongoMapper = partMongoMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String create(Participation participation) {
        return partMongoRepository.insert(partMongoMapper.participationToParticipationDocument(participation)).getId();
    }

    @Override
    public Optional<Participation> findById(String id) {
        return partMongoRepository.findById(id).map(partMongoMapper::participationDocumentToParticipation);
    }

    @Override
    public Optional<Participation> findByRoomIdAndUsername(String roomId, String username) {
        return partMongoRepository.findByRoomIdAndUsername(roomId, username).map(partMongoMapper::participationDocumentToParticipation);
    }

    @Override
    public List<Participation> findByRoomId(String roomId) {
        return partMongoRepository.findByRoomId(roomId).stream()
                .map(partMongoMapper::participationDocumentToParticipation)
                .toList();
    }

    @Override
    public List<Participation> findFinishedByRoomId(String roomId) {
        return partMongoRepository.findByRoomIdAndStatus(roomId, ParticipationStatus.FINISHED).stream()
                .map(partMongoMapper::participationDocumentToParticipation)
                .toList();
    }

    @Override
    public List<Participation> findReviewedByRoomId(String roomId) {
        return partMongoRepository.findByRoomIdAndReviewStatusIn(roomId, List.of(ReviewStatus.IA_REVIEWED, ReviewStatus.OWNER_REVIEWED)).stream()
                .map(partMongoMapper::participationDocumentToParticipation)
                .toList();
    }

    @Override
    public boolean existsByRoomIdAndUsername(String roomId, String username) {
        return partMongoRepository.existsByRoomIdAndUsername(roomId, username);
    }

    @Override
    public boolean existsStartedPartsByRoomId(String roomId) {
        return partMongoRepository.existsByRoomIdAndStatus(roomId, ParticipationStatus.STARTED);
    }

    @Override
    public boolean existsPendingReviewByRoomId(String roomId) {
        return partMongoRepository.existsByRoomIdAndReviewStatusIn(roomId, List.of(ReviewStatus.PENDING, ReviewStatus.IA_FAILED));
    }

    @Override
    public void update(Participation participation) {
        List<AnswerDocument> answerDocuments = participation.getAnswers().stream().map(partMongoMapper::answerToAnswerDocument).toList();
        partMongoRepository.update(participation.getId(), answerDocuments, participation.getFinishedAt(),
                participation.getStatus(), participation.getReviewStatus(), participation.getTotalTime(), participation.getTotalScore());
    }

    @Override
    public void updateReviewAnswers(String id, List<Answer> reviewAnswer, Integer totalScore, ReviewStatus reviewStatus) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update()
                .set("totalScore", totalScore)
                .set("reviewStatus", reviewStatus);
        IntStream.range(0, reviewAnswer.size()).forEach(i -> {
            Answer answer = reviewAnswer.get(i);
            String alias = "elem" + i;
            update.set("answers.$[" + alias + "].isCorrect", answer.getIsCorrect())
                  .set("answers.$[" + alias + "].score", answer.getScore())
                  .set("answers.$[" + alias + "].feedback", answer.getFeedback())
                  .filterArray(Criteria.where(alias + ".questionId").is(answer.getQuestionId()));
        });
        mongoTemplate.updateFirst(query, update, ParticipationDocument.class);
    }

    @Override
    public void delete(String id) {
        partMongoRepository.deleteById(id);
    }
}