package com.quizcode.module.participation.api.mapper;

import com.quizcode.module.participation.api.dto.answer.AnswerReviewRequest;
import com.quizcode.module.participation.api.dto.answer.AnswerRequest;
import com.quizcode.module.participation.api.dto.answer.AnswerResponse;
import com.quizcode.module.participation.api.dto.participation.LoginParticipationResponse;
import com.quizcode.module.participation.api.dto.participation.ParticipationRequest;
import com.quizcode.module.participation.api.dto.participation.PartRankingResponse;
import com.quizcode.module.participation.api.dto.participation.ParticipationResponse;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.answer.NewAnswer;
import com.quizcode.module.participation.domain.entity.answer.ReviewedAnswer;
import com.quizcode.module.participation.domain.entity.participation.SubmitParticipation;
import com.quizcode.module.participation.domain.entity.participation.NewParticipation;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.user.domain.PasswordHasher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipationMapper {

    private final PasswordHasher passwordHasher;

    public ParticipationMapper(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public Participation participationRequestToParticipation(String roomId, ParticipationRequest request) {
        return new NewParticipation(roomId, request.getUsername(), request.getPassword(), passwordHasher).getParticipation();
    }

    public ParticipationResponse participationToParticipationResponse(Participation participation) {
        List<AnswerResponse> answerResponses = participation.getAnswers().stream().map(this::answerToAnswerResponse).toList();
        return new ParticipationResponse(participation.getId(), participation.getRoomId(), participation.getUsername(), participation.getStatus(), participation.getReviewStatus(),
                participation.getStartedAt(), participation.getFinishedAt(), participation.getTotalScore(), participation.getTotalTime(), answerResponses);
    }

    public LoginParticipationResponse participationToLoginParticipationResponse(Participation participation) {
        return new LoginParticipationResponse(participation.getId(), participation.getStatus());
    }

    public PartRankingResponse participationToPartRankingResponse(Participation participation) {
        return new PartRankingResponse(participation.getUsername(), participation.getReviewStatus(), participation.getTotalScore(), participation.getTotalTime());
    }

    public Participation answerRequestsToParticipation(String id, String roomId, List<AnswerRequest> requests) {
        List<Answer> answers = requests.stream().map(this::answerRequestToAnswer).toList();
        return new SubmitParticipation(id, roomId, answers).getParticipation();
    }

    public Answer answerRequestToAnswer(AnswerRequest request) {
        return new NewAnswer(request.getQuestionId(), request.getCodeOptions(), request.getWrittenAnswer()).getAnswer();
    }

    public Answer answerReviewRequestToAnswer(AnswerReviewRequest request) {
        return new ReviewedAnswer(request.getQuestionId(), request.getIsCorrect(), request.getScore(), request.getFeedback()).getAnswer();
    }

    private AnswerResponse answerToAnswerResponse(Answer answer) {
        return new AnswerResponse(answer.getQuestionId(), answer.getCodeOptions(), answer.getWrittenAnswer(), answer.getIsCorrect(), answer.getScore(), answer.getFeedback());
    }
}
