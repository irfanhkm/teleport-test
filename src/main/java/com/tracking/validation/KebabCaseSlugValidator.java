package com.tracking.validation;

import com.tracking.model.TrackingNumberRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KebabCaseSlugValidator implements ConstraintValidator<KebabCaseSlug, TrackingNumberRequest> {

    @Override
    public boolean isValid(TrackingNumberRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getCustomerName() == null || request.getCustomerSlug() == null) {
            return true; // Let other validators handle null values
        }
        
        // Basic kebab-case validation
        return request.getCustomerSlug().matches("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    }
} 