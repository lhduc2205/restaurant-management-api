package com.lhduc.restaurantmanagementapi.common.anotation;

import com.lhduc.restaurantmanagementapi.common.validator.SortingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SortingValidator.class)
public @interface SortingConstraint {
    String message() default "must be a valid sort field, found: ${validatedValue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> sortClass();
}
