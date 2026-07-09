package com.quizcode.module.participation.infrastructure.adapter;

import com.quizcode.module.participation.domain.ParticipationAdapterService;
import com.quizcode.module.room.domain.port.RoomToParticipationPort;
import org.springframework.stereotype.Component;

@Component
public class ParticipationAdapter implements RoomToParticipationPort {

    private final ParticipationAdapterService participationAdapterService;

    public ParticipationAdapter(ParticipationAdapterService participationAdapterService) {
        this.participationAdapterService = participationAdapterService;
    }

    @Override
    public boolean hasStartedParticipations(String roomId) {
        return participationAdapterService.hasStartedParticipations(roomId);
    }

    @Override
    public boolean hasPendingReviews(String roomId) {
        return participationAdapterService.hasPendingReviews(roomId);
    }

    @Override
    public void deleteParticipationsByRoomId(String roomId) {
        participationAdapterService.deleteParticipationsByRoomId(roomId);
    }
}
