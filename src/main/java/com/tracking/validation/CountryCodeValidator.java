package com.tracking.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class CountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {
    private static final Set<String> VALID_COUNTRY_CODES = new HashSet<>();

    static {
        // Teleport only available in southeast asian countries
        VALID_COUNTRY_CODES.add("BN"); // Brunei
        VALID_COUNTRY_CODES.add("KH"); // Cambodia
        VALID_COUNTRY_CODES.add("ID"); // Indonesia
        VALID_COUNTRY_CODES.add("LA"); // Laos
        VALID_COUNTRY_CODES.add("MY"); // Malaysia
        VALID_COUNTRY_CODES.add("MM"); // Myanmar
        VALID_COUNTRY_CODES.add("PH"); // Philippines
        VALID_COUNTRY_CODES.add("SG"); // Singapore
        VALID_COUNTRY_CODES.add("TH"); // Thailand
        VALID_COUNTRY_CODES.add("TL"); // Timor-Leste
        VALID_COUNTRY_CODES.add("VN"); // Vietnam
    }

    @Override
    public boolean isValid(String countryCode, ConstraintValidatorContext context) {
        if (countryCode == null || countryCode.length() != 2) {
            return false;
        }
        return VALID_COUNTRY_CODES.contains(countryCode.toUpperCase());
    }
} 