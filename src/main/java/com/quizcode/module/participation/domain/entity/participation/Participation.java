package com.quizcode.module.participation.domain.entity.participation;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.shared.Util;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Getter
public class Participation {

    private final String id;
    private final String roomId;
    private final String username;
    private final String password;
    private final ParticipationStatus status;
    private final ReviewStatus reviewStatus;
    private final Instant startedAt;
    private final Instant finishedAt;
    private final Integer totalScore;
    private Long totalTime;
    private final List<Answer> answers;

    protected Participation(String id, String roomId, String username, String password, ParticipationStatus status, ReviewStatus reviewStatus, Instant startedAt, Instant finishedAt, Integer totalScore, Long totalTime, List<Answer> answers) {
        this.id = id;
        this.roomId = roomId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.reviewStatus = reviewStatus;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.totalScore = totalScore;
        this.totalTime = totalTime;
        this.answers = answers != null ? answers : List.of();
        validate();
    }

    public void calculateTotalTime(Instant startedAt) {
        this.totalTime = Duration.between(startedAt, this.finishedAt).toSeconds();
    }

    private void validate() {
        validateUsername();
        validateTotalScore();
        validateTotalTime();
    }

    private void validateUsername() {
        if (!Util.isNull(username) && username.length() > 20) {
            throw new InvalidDataExceptionCustom("El nombre de usuario no puede superar los 20 caracteres");
        }
    }

    private void validateTotalScore() {
        if (totalScore != null && totalScore < 0) {
            throw new InvalidDataExceptionCustom("La puntuación total no puede ser negativa");
        }
    }

    private void validateTotalTime() {
        if (totalTime != null && totalTime <= 0) {
            throw new InvalidDataExceptionCustom("El tiempo total debe ser mayor que 0");
        }
    }
}
