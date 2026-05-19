package com.quizcode.module.question.api.mapper;

import com.quizcode.module.question.api.dto.option.OptionRequest;
import com.quizcode.module.question.api.dto.option.OptionResponse;
import com.quizcode.module.question.api.dto.question.QuestionRequest;
import com.quizcode.module.question.api.dto.question.QuestionResponse;
import com.quizcode.module.question.domain.entity.question.EditQuestion;
import com.quizcode.module.question.domain.entity.question.NewQuestion;
import com.quizcode.module.question.domain.entity.option.Option;
import com.quizcode.module.question.domain.entity.question.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    default Question questionRequestToQuestion(String quizId, QuestionRequest request) {
        List<Option> options = request.getOptions() == null ? null : request.getOptions().stream().map(this::optionRequestToOption).toList();
        return new NewQuestion(quizId, request.getStatement(), request.getBaseCode(), request.getType(), request.getOrder(), request.getScore(), options).getQuestion();
    }

    default Question questionRequestToQuestion(String id, String quizId, QuestionRequest request) {
        List<Option> options = request.getOptions() == null ? null : request.getOptions().stream().map(this::optionRequestToOption).toList();
        return new EditQuestion(id, quizId, request.getStatement(), request.getBaseCode(), request.getType(), request.getOrder(), request.getScore(), options).getQuestion();
    }

    QuestionResponse questionToQuestionResponse(Question question);

    OptionResponse optionToOptionResponse(Option option);

    Option optionRequestToOption(OptionRequest request);
}
