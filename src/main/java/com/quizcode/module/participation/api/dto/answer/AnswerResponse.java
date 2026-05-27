package com.quizcode.module.participation.api.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private String questionId;
    private List<String> codeOptions;
    private String writtenAnswer;
    private Boolean isCorrect;
    private Integer score;
    private String feedback;
}
