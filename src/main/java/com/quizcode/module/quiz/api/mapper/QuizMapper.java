package com.quizcode.module.quiz.api.mapper;

import com.quizcode.module.quiz.api.dto.QuizRequest;
import com.quizcode.module.quiz.api.dto.QuizResponse;
import com.quizcode.module.quiz.domain.entity.EditQuiz;
import com.quizcode.module.quiz.domain.entity.NewQuiz;
import com.quizcode.module.quiz.domain.entity.Quiz;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    default Quiz quizRequestToQuiz(String ownerId, QuizRequest request) {
        return new NewQuiz(ownerId, request.getTitle(), request.getDescription(), request.getHasLimit(), request.getLimitMinutes()).getQuiz();
    }

    default Quiz quizRequestToQuiz(String id, String ownerId, QuizRequest request) {
        return new EditQuiz(id, ownerId, request.getTitle(), request.getDescription(), request.getHasLimit(), request.getLimitMinutes()).getQuiz();
    }

    QuizResponse quizToQuizResponse(Quiz quiz);
}
