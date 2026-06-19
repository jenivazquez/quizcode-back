package com.quizcode.module.participation.infrastructure.adapter;

import com.quizcode.module.participation.domain.ParticipationAdapterService;
import com.quizcode.module.question.domain.QuestionToParticipationPort;
import org.springframework.stereotype.Component;

@Component
public class QuestionParticipationAdapter implements QuestionToParticipationPort {

    private final ParticipationAdapterService participationAdapterService;

    public QuestionParticipationAdapter(ParticipationAdapterService participationAdapterService) {
        this.participationAdapterService = participationAdapterService;
    }

    @Override
    public boolean isParticipationStarted(String participationId) {
        return participationAdapterService.isParticipationStarted(participationId);
    }

    @Override
    public boolean isParticipationFinished(String participationId) {
        return participationAdapterService.isParticipationFinished(participationId);
    }
}
