package com.quizcode.module.participation.application;

import com.quizcode.module.participation.domain.ParticipationAdapterService;
import com.quizcode.module.participation.domain.ParticipationRepository;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import org.springframework.stereotype.Service;

@Service
public class ParticipationAdapterServiceImpl implements ParticipationAdapterService {

    private final ParticipationRepository participationRepository;

    public ParticipationAdapterServiceImpl(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    @Override
    public boolean hasStartedParticipations(String roomId) {
        return participationRepository.existsStartedPartsByRoomId(roomId);
    }

    @Override
    public boolean hasPendingReviews(String roomId) {
        return participationRepository.existsPendingReviewByRoomId(roomId);
    }

    @Override
    public boolean isParticipationStarted(String participationId) {
        return participationRepository.findById(participationId)
                .map(p -> ParticipationStatus.STARTED.equals(p.getStatus()))
                .orElse(false);
    }

    @Override
    public boolean isParticipationFinished(String participationId) {
        return participationRepository.findById(participationId)
                .map(p -> ParticipationStatus.FINISHED.equals(p.getStatus()))
                .orElse(false);
    }

    @Override
    public void deleteParticipationsByRoomId(String roomId) {
        participationRepository.deleteByRoomId(roomId);
    }
}
