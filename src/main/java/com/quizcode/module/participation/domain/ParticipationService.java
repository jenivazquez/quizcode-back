package com.quizcode.module.participation.domain;

import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.participation.Participation;

import java.util.List;

public interface ParticipationService {
    String create(Participation participation);
    String login(String roomId, String username, String password);
    Participation findById(String roomId, String participationId);
    void submitAnswers(Participation participation);
    List<Participation> findByRoomIdToRanking(String roomId);
    List<Participation> findByRoomIdAsRoomOwner(String ownerId, String quizId, String roomId);
    Participation findByIdAsRoomOwner(String ownerId, String quizId, String roomId, String participationId);
    void updateReviewAsRoomOwner(String ownerId, String quizId, String roomId, String participationId, List<Answer> corrections);
    void deleteAsRoomOwner(String ownerId, String quizId, String roomId, String participationId);
}
