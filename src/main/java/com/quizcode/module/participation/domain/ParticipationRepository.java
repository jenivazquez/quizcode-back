package com.quizcode.module.participation.domain;

import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository {
    String create(Participation participation);
    Optional<Participation> findById(String id);
    Optional<Participation> findByRoomIdAndUsername(String roomId, String username);
    List<Participation> findByRoomId(String roomId);
    List<Participation> findFinishedByRoomId(String roomId);
    List<Participation> findReviewedByRoomId(String roomId);
    boolean existsByRoomIdAndUsername(String roomId, String username);
    boolean existsStartedPartsByRoomId(String roomId);
    boolean existsPendingReviewByRoomId(String roomId);
    void update(Participation participation);
    void updateReviewAnswers(String id, List<Answer> corrections, Integer totalScore, ReviewStatus reviewStatus);
    void delete(String id);
    void deleteByRoomId(String roomId);
}
