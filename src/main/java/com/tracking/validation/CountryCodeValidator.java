package com.tracking.validation;

import com.tracking.model.TrackingNumberRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryCodeValidator implements ConstraintValidator<DifferentCountryCodes, TrackingNumberRequest> {

    @Override
    public boolean isValid(TrackingNumberRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getOriginCountryId() == null || request.getDestinationCountryId() == null) {
            return true; // Let other validators handle null values
        }
        
        return !request.getOriginCountryId().equals(request.getDestinationCountryId());
    }
} 