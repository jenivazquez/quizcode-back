package com.quizcode.module.participation.domain.entity.answer;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.QuizConstants;
import com.quizcode.shared.Util;
import lombok.Getter;

import java.util.List;

@Getter
public class Answer {

    private final String questionId;
    private final List<String> codeOptions;
    private final String writtenAnswer;
    private final Boolean isCorrect;
    private final Integer score;
    private final String feedback;

    protected Answer(String questionId, List<String> codeOptions, String writtenAnswer, Boolean isCorrect, Integer score, String feedback) {
        this.questionId = questionId;
        this.codeOptions = codeOptions;
        this.writtenAnswer = writtenAnswer;
        this.isCorrect = isCorrect;
        this.score = score;
        this.feedback = feedback;
        validate();
    }

    private void validate() {
        validateCodeOptions();
        validateScore();
        validateFeedback();
    }

    private void validateCodeOptions() {
        if (!Util.isNull(codeOptions) && codeOptions.stream().anyMatch(c -> !QuizConstants.ALLOWED_OPTION_CODES.contains(c))) {
            throw new InvalidDataExceptionCustom("Los códigos de opción seleccionados no están entre los valores permitidos");
        }
    }

    private void validateScore() {
        if (score != null && score < 0) {
            throw new InvalidDataExceptionCustom("La puntuación no puede ser negativa");
        }
    }

    private void validateFeedback() {
        if (!Util.isNull(feedback) && feedback.length() > 500) {
            throw new InvalidDataExceptionCustom("El feedback no puede superar los 500 caracteres");
        }
    }
}
