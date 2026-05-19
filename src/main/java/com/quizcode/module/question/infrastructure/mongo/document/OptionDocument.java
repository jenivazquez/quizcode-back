package com.quizcode.module.question.infrastructure.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDocument {
    private String code;
    private String value;
    private Boolean isValid;
}
