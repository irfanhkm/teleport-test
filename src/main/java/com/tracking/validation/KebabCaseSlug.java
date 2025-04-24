package com.tracking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KebabCaseSlugValidator.class)
public @interface KebabCaseSlug {
    String message() default "Customer slug must be a kebab-case version of the customer name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 