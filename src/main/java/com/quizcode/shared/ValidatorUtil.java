package com.quizcode.shared;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ValidatorUtil {

    public static void validateFieldsNotNull(Map<String, Object> fields) {
        fields.forEach((name, value) -> {
            if (Util.isNull(value)) throw new InvalidDataExceptionCustom("El campo " + name + " es obligatorio");
        });
    }

    public static void validateFieldsNotNullAsGroup(List<Object> fields) {
        boolean anyNull = fields.stream().anyMatch(Util::isNull);
        if (anyNull) throw new InvalidDataExceptionCustom("Se han detectado datos inconsistentes");
    }
}
