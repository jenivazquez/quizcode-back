package com.quizcode.module.question.infrastructure.mongo.mapper;

import com.quizcode.module.question.domain.entity.option.Option;
import com.quizcode.module.question.domain.entity.question.Question;
import com.quizcode.module.question.domain.entity.question.SavedQuestion;
import com.quizcode.module.question.infrastructure.mongo.document.OptionDocument;
import com.quizcode.module.question.infrastructure.mongo.document.QuestionDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMongoMapper {

    default Question questionDocumentToQuestion(QuestionDocument doc) {
        List<Option> options = doc.getOptions() == null ? null : doc.getOptions().stream().map(this::optionDocumentToOption).toList();
        return new SavedQuestion(doc.getId(), doc.getQuizId(), doc.getStatement(), doc.getBaseCode(), doc.getType(), doc.getOrder(), doc.getScore(), options).getQuestion();
    }

    Option optionDocumentToOption(OptionDocument doc);

    QuestionDocument questionToQuestionDocument(Question question);

    OptionDocument optionToOptionDocument(Option option);
}
