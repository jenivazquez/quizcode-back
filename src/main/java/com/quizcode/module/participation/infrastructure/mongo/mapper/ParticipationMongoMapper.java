package com.quizcode.module.participation.infrastructure.mongo.mapper;

import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.answer.SavedAnswer;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.participation.domain.entity.participation.SavedParticipation;
import com.quizcode.module.participation.infrastructure.mongo.document.AnswerDocument;
import com.quizcode.module.participation.infrastructure.mongo.document.ParticipationDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationMongoMapper {

    default Participation participationDocumentToParticipation(ParticipationDocument doc) {
        List<Answer> answers = doc.getAnswers() == null ? List.of() : doc.getAnswers().stream().map(this::answerDocumentToAnswer).toList();
        return new SavedParticipation(doc.getId(), doc.getRoomId(), doc.getUsername(), doc.getPassword(), doc.getStatus(), doc.getReviewStatus(),
                doc.getStartedAt(), doc.getFinishedAt(), doc.getTotalScore(), doc.getTotalTime(), answers).getParticipation();
    }

    default Answer answerDocumentToAnswer(AnswerDocument doc) {
        return new SavedAnswer(doc.getQuestionId(), doc.getCodeOptions(), doc.getWrittenAnswer(), doc.getIsCorrect(), doc.getScore(), doc.getFeedback()).getAnswer();
    }

    ParticipationDocument participationToParticipationDocument(Participation participation);

    AnswerDocument answerToAnswerDocument(Answer answer);
}
