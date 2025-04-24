package com.tracking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountryCodeValidator.class)
public @interface DifferentCountryCodes {
    String message() default "Origin country ID must be different from destination country ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 