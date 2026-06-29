package com.quizcode.module.question.api.mapper;

import com.quizcode.module.question.api.dto.option.AIOptionResponse;
import com.quizcode.module.question.api.dto.question.AIQuestionResponse;
import com.quizcode.module.question.api.dto.message.MessageRequest;
import com.quizcode.module.question.api.dto.option.OptionRequest;
import com.quizcode.module.question.api.dto.option.OptionResponse;
import com.quizcode.module.question.api.dto.option.OptionToAnswerResponse;
import com.quizcode.module.question.api.dto.question.QuestionRequest;
import com.quizcode.module.question.api.dto.question.QuestionResponse;
import com.quizcode.module.question.api.dto.question.QuestionToAnswerResponse;
import com.quizcode.module.question.domain.entity.option.AIOption;
import com.quizcode.module.question.domain.entity.message.AIMessage;
import com.quizcode.module.question.domain.entity.question.AIQuestion;
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

    QuestionToAnswerResponse questionToQuestionToAnswerResponse(Question question);

    OptionResponse optionToOptionResponse(Option option);

    AIOptionResponse aiOptionToAiOptionResponse(AIOption option);

    OptionToAnswerResponse optionToOptionToAnswerResponse(Option option);

    Option optionRequestToOption(OptionRequest request);

    AIMessage messageRequestToAiMessage(MessageRequest request);

    default AIQuestionResponse aiQuestionToAIQuestionResponse(AIQuestion aiQuestion) {
        List<AIOptionResponse> options = aiQuestion.options() == null ? null : aiQuestion.options().stream().map(this::aiOptionToAiOptionResponse).toList();
        return new AIQuestionResponse(aiQuestion.message(), aiQuestion.statement(), aiQuestion.baseCode(), aiQuestion.type(), aiQuestion.score(), options);
    }
}
