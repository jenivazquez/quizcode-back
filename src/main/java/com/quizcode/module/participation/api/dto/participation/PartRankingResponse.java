package com.quizcode.module.participation.api.dto.participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRankingResponse {
    private String username;
    private Integer totalScore;
    private Long totalTime;
}
