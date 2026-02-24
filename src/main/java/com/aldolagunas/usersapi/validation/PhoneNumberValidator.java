package com.aldolagunas.usersapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^(\\+?\\d{1,3})?[\\s\\-]?\\d{2}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}$|^(\\+?\\d{1,3})?[\\s\\-]?\\d{10}$");

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.trim().isEmpty()) return false;
        String digitsOnly = value.replaceAll("[\\s\\-+]", "");
        long digitCount = digitsOnly.chars().filter(Character::isDigit).count();
        return PHONE_NUMBER_PATTERN.matcher(value).matches() && digitCount >= 10;
    }
}