package com.quizcode.module.participation.domain.entity.participation;

import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Getter
public class SavedParticipation {

    private final Participation participation;

    public SavedParticipation(String id, String roomId, String username, String password, ParticipationStatus status, ReviewStatus reviewStatus, Instant startedAt, Instant finishedAt, Integer totalScore, Long totalTime, List<Answer> answers) {
        validate(id, roomId, username, password, status, startedAt);
        this.participation = new Participation(id, roomId, username, password, status, reviewStatus, startedAt, finishedAt, totalScore, totalTime, answers);
    }

    private void validate(String id, String roomId, String username, String password, ParticipationStatus status, Instant startedAt) {
        ValidatorUtil.validateFieldsNotNullAsGroup(Arrays.asList(id, roomId, username, password, status, startedAt));
    }
}
