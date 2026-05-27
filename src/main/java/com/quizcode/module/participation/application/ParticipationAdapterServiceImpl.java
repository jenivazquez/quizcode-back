package com.quizcode.module.participation.application;

import com.quizcode.module.participation.domain.ParticipationAdapterService;
import com.quizcode.module.participation.domain.ParticipationRepository;
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
}
