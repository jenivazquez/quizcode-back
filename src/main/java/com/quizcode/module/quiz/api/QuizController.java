package com.quizcode.module.quiz.api;

import com.quizcode.module.quiz.api.dto.IdQuizResponse;
import com.quizcode.module.quiz.api.dto.QuizRequest;
import com.quizcode.module.quiz.api.dto.QuizResponse;
import com.quizcode.module.quiz.api.dto.UpdateQuizStatusRequest;
import com.quizcode.module.quiz.api.mapper.QuizMapper;
import com.quizcode.module.quiz.domain.QuizService;
import com.quizcode.shared.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class QuizController {

    private final QuizService quizService;
    private final QuizMapper quizMapper;

    public QuizController(QuizService quizService, QuizMapper quizMapper) {
        this.quizService = quizService;
        this.quizMapper = quizMapper;
    }

    @PostMapping(path = "/user/{ownerId}/quiz")
    @ResponseStatus(HttpStatus.CREATED)
    public IdQuizResponse create(@PathVariable String ownerId, @RequestBody QuizRequest quizRequest) {
        SecurityUtil.checkAuthorized(ownerId);
        String idQuiz = quizService.create(quizMapper.quizRequestToQuiz(ownerId, quizRequest));
        return new IdQuizResponse(idQuiz);
    }

    @GetMapping(path = "/user/{ownerId}/quiz")
    @ResponseStatus(HttpStatus.OK)
    public List<QuizResponse> findByOwnerId(@PathVariable String ownerId) {
        SecurityUtil.checkAuthorized(ownerId);
        return quizService.findByOwnerId(ownerId).stream()
                .map(quizMapper::quizToQuizResponse)
                .toList();
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuizResponse findById(@PathVariable String ownerId, @PathVariable String id) {
        SecurityUtil.checkAuthorized(ownerId);
        return quizMapper.quizToQuizResponse(quizService.findById(id, ownerId));
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String ownerId, @PathVariable String id, @RequestBody QuizRequest quizRequest) {
        SecurityUtil.checkAuthorized(ownerId);
        quizService.update(quizMapper.quizRequestToQuiz(id, ownerId, quizRequest));
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable String ownerId, @PathVariable String id, @RequestBody UpdateQuizStatusRequest statusRequest) {
        SecurityUtil.checkAuthorized(ownerId);
        quizService.updateStatus(id, ownerId, statusRequest.getStatus());
    }

    @DeleteMapping(path = "/user/{ownerId}/quiz/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String ownerId, @PathVariable String id) {
        SecurityUtil.checkAuthorized(ownerId);
        quizService.delete(id, ownerId);
    }
}
