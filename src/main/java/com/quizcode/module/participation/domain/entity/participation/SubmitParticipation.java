package com.quizcode.module.participation.domain.entity.participation;

import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SubmitParticipation {

    private final Participation participation;

    public SubmitParticipation(String id, String roomId, List<Answer> answers) {
        validate(id, roomId, answers);
        this.participation = new Participation(id, roomId, null, null, ParticipationStatus.FINISHED, ReviewStatus.PENDING, null, Instant.now(), null, null, answers);
    }

    private void validate(String id, String roomId, List<Answer> answers) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la participación", id);
        fields.put("identificador de la sala", roomId);
        fields.put("respuestas", answers);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
