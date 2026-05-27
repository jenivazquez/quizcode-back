package com.quizcode.module.participation.api;

import com.quizcode.module.participation.api.dto.answer.AnswerReviewRequest;
import com.quizcode.module.participation.api.dto.answer.AnswerRequest;
import com.quizcode.module.participation.api.dto.participation.ParticipationRequest;
import com.quizcode.module.participation.api.dto.participation.IdParticipationResponse;
import com.quizcode.module.participation.api.dto.participation.PartRankingResponse;
import com.quizcode.module.participation.api.dto.participation.ParticipationResponse;
import com.quizcode.module.participation.api.mapper.ParticipationMapper;
import com.quizcode.module.participation.domain.ParticipationService;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.answer.ReviewedAnswer;
import com.quizcode.shared.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ParticipationController {

    private final ParticipationService participationService;
    private final ParticipationMapper participationMapper;

    public ParticipationController(ParticipationService participationService, ParticipationMapper participationMapper) {
        this.participationService = participationService;
        this.participationMapper = participationMapper;
    }

    @PostMapping(path = "/room/{roomId}/participation")
    @ResponseStatus(HttpStatus.CREATED)
    public IdParticipationResponse create(@PathVariable String roomId, @RequestBody ParticipationRequest request) {
        String id = participationService.create(participationMapper.participationRequestToParticipation(roomId, request));
        return new IdParticipationResponse(id);
    }

    @PostMapping(path = "/room/{roomId}/participation/login")
    @ResponseStatus(HttpStatus.OK)
    public IdParticipationResponse login(@PathVariable String roomId, @RequestBody ParticipationRequest request) {
        return new IdParticipationResponse(participationService.login(roomId, request.getUsername(), request.getPassword()));
    }

    @GetMapping(path = "/room/{roomId}/participation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationResponse findById(@PathVariable String roomId, @PathVariable String id) {
        return participationMapper.participationToParticipationResponse(participationService.findById(roomId, id));
    }

    @PatchMapping(path = "/room/{roomId}/participation/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitAnswers(@PathVariable String roomId, @PathVariable String id, @RequestBody List<AnswerRequest> request) {
        participationService.submitAnswers(participationMapper.answerRequestsToParticipation(id, roomId, request));
    }

    @GetMapping(path = "/room/{roomId}/participation/ranking")
    @ResponseStatus(HttpStatus.OK)
    public List<PartRankingResponse> findByRoomIdToRanking(@PathVariable String roomId) {
        return participationService.findByRoomIdToRanking(roomId).stream()
                .map(participationMapper::participationToPartRankingResponse)
                .toList();
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{roomId}/participation")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationResponse> findByRoomIdAsRoomOwner(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String roomId) {
        SecurityUtil.checkAuthorized(ownerId);
        return participationService.findByRoomIdAsRoomOwner(ownerId, quizId, roomId).stream()
                .map(participationMapper::participationToParticipationResponse)
                .toList();
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{roomId}/participation/{participationId}")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationResponse findByIdAsRoomOwner(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String roomId, @PathVariable String participationId) {
        SecurityUtil.checkAuthorized(ownerId);
        return participationMapper.participationToParticipationResponse(participationService.findByIdAsRoomOwner(ownerId, quizId, roomId, participationId));
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{roomId}/participation/{participationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReviewAsRoomOwner(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String roomId, @PathVariable String participationId, @RequestBody List<AnswerReviewRequest> request) {
        SecurityUtil.checkAuthorized(ownerId);
        List<Answer> answers = request.stream().map(participationMapper::answerReviewRequestToAnswer).toList();
        participationService.updateReviewAsRoomOwner(ownerId, quizId, roomId, participationId, answers);
    }

    @DeleteMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{roomId}/participation/{participationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsRoomOwner(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String roomId, @PathVariable String participationId) {
        SecurityUtil.checkAuthorized(ownerId);
        participationService.deleteAsRoomOwner(ownerId, quizId, roomId, participationId);
    }
}
