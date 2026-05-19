package com.quizcode.module.question.domain.entity.question;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.question.domain.entity.option.Option;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import com.quizcode.shared.Util;
import lombok.Getter;

import java.util.List;

@Getter
public class Question {

    private final String id;
    private final String statement;
    private final String baseCode;
    private final QuestionType type;
    private final Integer order;
    private final Integer score;
    private final String quizId;
    private final List<Option> options;

    protected Question(String id, String statement, String baseCode, QuestionType type, Integer order, Integer score, String quizId, List<Option> options) {
        this.id = id;
        this.statement = statement;
        this.baseCode = baseCode;
        this.type = type;
        this.order = order;
        this.score = score;
        this.quizId = quizId;
        this.options = options != null ? options : List.of();
        validate();
    }

    private void validate() {
        validateStatement();
        validateRequiredFieldsByType();
        validateScore();
        validateOrder();
    }

    private void validateStatement() {
        if (!Util.isNull(statement) && statement.length() > 500) {
            throw new InvalidDataExceptionCustom("El enunciado de la pregunta no puede superar los 500 caracteres");
        }
    }

    private void validateScore() {
        if (score != null && score <= 0) {
            throw new InvalidDataExceptionCustom("La puntuación debe ser mayor que 0");
        }
    }

    private void validateOrder() {
        if (order != null && order <= 0) {
            throw new InvalidDataExceptionCustom("El orden de la pregunta debe ser mayor que 0");
        }
    }

    private void validateRequiredFieldsByType() {
        if(type != null) {
            boolean requiresCode = type == QuestionType.EDIT_CODE;
            boolean requiresOptions = type == QuestionType.SINGLE_CHOICE || type == QuestionType.MULTIPLE_CHOICE;
            if (requiresCode && Util.isNull(baseCode)) throw new InvalidDataExceptionCustom("Este tipo de pregunta requiere código base");
            if (!requiresOptions && !Util.isNull(options)) throw new InvalidDataExceptionCustom("Este tipo de pregunta no admite opciones");
            if (requiresOptions && Util.isNull(options)) throw new InvalidDataExceptionCustom("Este tipo de pregunta requiere opciones");
            if (requiresOptions) validateOptionIsValid();
        }
    }

    private void validateOptionIsValid() {
        if (type == QuestionType.SINGLE_CHOICE && options.size() < 2) throw new InvalidDataExceptionCustom("La pregunta de opción simple debe tener al menos 2 opciones");
        if (type == QuestionType.MULTIPLE_CHOICE && options.size() < 3) throw new InvalidDataExceptionCustom("La pregunta de opción múltiple debe tener al menos 3 opciones");
        long numValidOptions = options.stream().filter(o -> Boolean.TRUE.equals(o.getIsValid())).count();
        if (type == QuestionType.SINGLE_CHOICE && numValidOptions != 1) throw new InvalidDataExceptionCustom("La pregunta de opción simple debe tener exactamente una opción correcta");
        if (type == QuestionType.MULTIPLE_CHOICE && numValidOptions < 1) throw new InvalidDataExceptionCustom("La pregunta de opción múltiple debe tener al menos una opción correcta");
    }
}
