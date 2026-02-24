package com.aldolagunas.usersapi.validation;

import com.aldolagunas.usersapi.validation.RfcFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;


public class RfcValidator implements ConstraintValidator<RfcFormat, String> {

    private static final Pattern RFC_PATTERN = Pattern.compile("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if(value == null || value.trim().isEmpty()) return false;
        return RFC_PATTERN.matcher(value.trim().toUpperCase()).matches();
    }
}