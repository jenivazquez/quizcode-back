package com.quizcode.module.question.api;

import com.quizcode.module.question.api.dto.IdQuestionResponse;
import com.quizcode.module.question.api.dto.question.QuestionRequest;
import com.quizcode.module.question.api.dto.question.QuestionResponse;
import com.quizcode.module.question.api.mapper.QuestionMapper;
import com.quizcode.module.question.domain.QuestionService;
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
        SecurityUtil.checkAuthorized(ownerId);
        String idQuestion = questionService.create(ownerId, questionMapper.questionRequestToQuestion(quizId, request));
        return new IdQuestionResponse(idQuestion);
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{quizId}/question")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionResponse> findByQuizId(@PathVariable String ownerId, @PathVariable String quizId) {
        SecurityUtil.checkAuthorized(ownerId);
        return questionService.findByQuizId(ownerId, quizId).stream()
                .map(questionMapper::questionToQuestionResponse)
                .toList();
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{quizId}/question/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id, @RequestBody QuestionRequest request) {
        SecurityUtil.checkAuthorized(ownerId);
        questionService.update(ownerId, questionMapper.questionRequestToQuestion(id, quizId, request));
    }

    @DeleteMapping(path = "/user/{ownerId}/quiz/{quizId}/question/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id) {
        SecurityUtil.checkAuthorized(ownerId);
        questionService.delete(ownerId, quizId, id);
    }
}
