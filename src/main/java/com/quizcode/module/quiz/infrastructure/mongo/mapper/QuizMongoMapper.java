package com.quizcode.module.quiz.infrastructure.mongo.mapper;

import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.SavedQuiz;
import com.quizcode.module.quiz.infrastructure.mongo.document.QuizDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizMongoMapper {

    default Quiz quizDocumentToQuiz(QuizDocument doc) {
        return new SavedQuiz(doc.getId(), doc.getOwnerId(), doc.getTitle(), doc.getDescription(), doc.getHasLimit(), doc.getLimitMinutes(), doc.getStatus(), doc.getCreatedAt(), doc.getUpdatedAt()).getQuiz();
    }

    QuizDocument quizToQuizDocument(Quiz quiz);
}
