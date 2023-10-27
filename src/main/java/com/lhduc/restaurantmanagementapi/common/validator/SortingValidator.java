package com.lhduc.restaurantmanagementapi.common.validator;

import com.lhduc.restaurantmanagementapi.common.anotation.SortingConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class SortingValidator implements ConstraintValidator<SortingConstraint, String> {
    private SortingConstraint sortingConstraint;
    private static final String FIELD_SEPARATOR = ",";
    private static final String DESC_SIGN = "-";

    @Override
    public void initialize(SortingConstraint constraintAnnotation) {
        this.sortingConstraint = constraintAnnotation;
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Class<?> sortingClass = sortingConstraint.sortClass();
        String[] requestedFields = value.split(FIELD_SEPARATOR);

        for (String sortField : requestedFields) {
            boolean isValid = this.checkValidField(sortField, sortingClass);

            if (!isValid) {
                return false;
            }
        }

        return true;
    }

    private boolean checkValidField(String fieldName, Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            String declaredFieldName = field.getName();
            if (fieldName.equals(declaredFieldName) || fieldName.equals(DESC_SIGN + declaredFieldName)) {
                return true;
            }
        }

        return false;
    }
}
