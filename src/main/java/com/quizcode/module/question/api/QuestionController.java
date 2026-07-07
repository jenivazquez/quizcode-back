package com.quizcode.module.question.api;

import com.quizcode.module.question.api.dto.IdQuestionResponse;
import com.quizcode.module.question.api.dto.question.AIQuestionResponse;
import com.quizcode.module.question.api.dto.message.MessageRequest;
import com.quizcode.module.question.api.dto.question.QuestionRequest;
import com.quizcode.module.question.api.dto.question.QuestionResponse;
import com.quizcode.module.question.api.dto.question.QuestionToAnswerResponse;
import com.quizcode.module.question.api.mapper.QuestionMapper;
import com.quizcode.module.question.domain.QuestionService;
import com.quizcode.module.question.domain.entity.message.AIMessage;

import com.quizcode.shared.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionController(QuestionService questionService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    @PostMapping(path = "/user/{ownerId}/quiz/{quizId}/question")
    @ResponseStatus(HttpStatus.CREATED)
    public IdQuestionResponse create(@PathVariable String ownerId, @PathVariable String quizId, @RequestBody QuestionRequest request) {
        SecurityUtil.checkAuthorizedUser(ownerId);
        String idQuestion = questionService.create(ownerId, questionMapper.questionRequestToQuestion(quizId, request));
        return new IdQuestionResponse(idQuestion);
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{quizId}/question")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionResponse> findByQuizId(@PathVariable String ownerId, @PathVariable String quizId) {
        SecurityUtil.checkAuthorizedUser(ownerId);
        return questionService.findByQuizId(ownerId, quizId).stream()
                .map(questionMapper::questionToQuestionResponse)
                .toList();
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{quizId}/question/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id, @RequestBody QuestionRequest request) {
        SecurityUtil.checkAuthorizedUser(ownerId);
        questionService.update(ownerId, questionMapper.questionRequestToQuestion(id, quizId, request));
    }

    @DeleteMapping(path = "/user/{ownerId}/quiz/{quizId}/question/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id) {
        SecurityUtil.checkAuthorizedUser(ownerId);
        questionService.delete(ownerId, quizId, id);
    }

    @GetMapping(path = "/quiz/{quizId}/question")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionToAnswerResponse> findByQuizIdToAnswer(@PathVariable String quizId, @RequestParam String partId) {
        SecurityUtil.checkAuthorizedPart(partId);
        return questionService.findByQuizIdToAnswer(quizId, partId).stream()
                .map(questionMapper::questionToQuestionToAnswerResponse)
                .toList();
    }

    @GetMapping(path = "/quiz/{quizId}/question/review")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionResponse> findByQuizIdToReview(@PathVariable String quizId, @RequestParam String partId) {
        SecurityUtil.checkAuthorizedPart(partId);
        return questionService.findByQuizIdToReview(quizId, partId).stream()
                .map(questionMapper::questionToQuestionResponse)
                .toList();
    }

    @PostMapping(path = "/user/{ownerId}/quiz/{quizId}/question/generate")
    @ResponseStatus(HttpStatus.OK)
    public AIQuestionResponse generateAIQuestion(@PathVariable String ownerId, @PathVariable String quizId, @RequestBody List<MessageRequest> messages) {
        SecurityUtil.checkAuthorizedUser(ownerId);
        List<AIMessage> AIMessages = messages.stream().map(questionMapper::messageRequestToAiMessage).toList();
        return questionMapper.aiQuestionToAIQuestionResponse(questionService.generateAIQuestion(ownerId, quizId, AIMessages));
    }
}
