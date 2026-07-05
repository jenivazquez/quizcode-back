package com.quizcode.module.participation.api.dto.participation;

import com.quizcode.module.participation.api.dto.answer.AnswerResponse;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationResponse {
    private String id;
    private String roomId;
    private String username;
    private ParticipationStatus status;
    private ReviewStatus reviewStatus;
    private Instant startedAt;
    private Instant finishedAt;
    private Integer totalScore;
    private Long totalTime;
    private List<AnswerResponse> answers;
}
