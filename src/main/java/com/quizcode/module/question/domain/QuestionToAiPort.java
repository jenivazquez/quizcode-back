package com.quizcode.module.question.domain;

import com.quizcode.module.question.domain.entity.message.AIMessage;
import com.quizcode.module.question.domain.entity.question.AIQuestion;
import com.quizcode.module.question.domain.entity.question.Question;

import java.util.List;

public interface QuestionToAiPort {
    AIQuestion generateQuestion(List<Question> savedQuestions, List<AIMessage> messages);
}
