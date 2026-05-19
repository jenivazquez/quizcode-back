package com.quizcode.module.question.api.dto.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionRequest {
    private String code;
    private String value;
    private Boolean isValid;
}
