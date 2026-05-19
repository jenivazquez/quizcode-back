package com.quizcode.module.question.domain.entity.option;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Option {

    private final String code;
    private final String value;
    private final Boolean isValid;

    public Option(String code, String value, Boolean isValid) {
        this.code = code;
        this.value = value;
        this.isValid = isValid;
        validate();
    }

    private void validate() {
        validateRequiredFields();
        if (value.length() > 200) throw new InvalidDataExceptionCustom("El valor de la opción no puede superar los 200 caracteres");
    }

    private void validateRequiredFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("código de opción", code);
        fields.put("valor de la opción", value);
        fields.put("campo válida de la opción", isValid);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
