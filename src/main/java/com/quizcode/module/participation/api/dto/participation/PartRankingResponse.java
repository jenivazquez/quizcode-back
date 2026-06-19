package com.quizcode.module.participation.api.dto.participation;

import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRankingResponse {
    private String username;
    private ReviewStatus reviewStatus;
    private Integer totalScore;
    private Long totalTime;
}
