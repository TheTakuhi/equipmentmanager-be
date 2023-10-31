package com.interstellar.equipmentmanager.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class AlphaStringValidator implements ConstraintValidator<AlphaString, String> {
    private final String regexStr = "^[\\p{L}\\s-`]+$";
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) return true;
        var regex = Pattern.compile(regexStr, Pattern.UNICODE_CHARACTER_CLASS);
        return regex.matcher(s).find();
    }
}
