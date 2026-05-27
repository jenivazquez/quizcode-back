package com.quizcode.module.question.infrastructure.adapter;

import com.quizcode.module.participation.domain.ParticipationToQuestionPort;
import com.quizcode.module.participation.domain.entity.question.QuestionSummary;
import com.quizcode.module.question.domain.QuestionAdapterService;
import com.quizcode.module.question.domain.entity.option.Option;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipationQuestionAdapter implements ParticipationToQuestionPort {

    private final QuestionAdapterService questionAdapterService;

    public ParticipationQuestionAdapter(QuestionAdapterService questionAdapterService) {
        this.questionAdapterService = questionAdapterService;
    }

    @Override
    public boolean questionBelongsToQuiz(String questionId, String quizId) {
        return questionAdapterService.existsByQuizIdAndId(quizId, questionId);
    }

    @Override
    public List<QuestionSummary> findByQuizId(String quizId) {
        return questionAdapterService.findByQuizId(quizId).stream()
                .map(question -> new QuestionSummary(
                        question.getId(),
                        question.getType().name(),
                        question.getOptions().stream()
                                .filter(o -> Boolean.TRUE.equals(o.getIsValid()))
                                .map(Option::getCode)
                                .toList(),
                        question.getScore()
                ))
                .toList();
    }
}
