package com.quizcode.shared;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Util {

    public static boolean isNull(Object value) {
        if (value == null) return true;
        if (value instanceof String text) return text.isBlank();
        if (value instanceof List list) return list.isEmpty();
        return false;
    }
}
