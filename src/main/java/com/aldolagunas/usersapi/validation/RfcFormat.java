package com.aldolagunas.usersapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RfcValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RfcFormat {
    String message() default "Invalid RFC format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}