package com.quizcode.module.question.domain.entity.question;

import com.quizcode.module.question.domain.entity.option.AIOption;
import com.quizcode.module.question.domain.entity.type.QuestionType;

import java.util.List;

public record AIQuestion(String message, String statement, QuestionType type, Integer score, String baseCode, List<AIOption> options) {}
