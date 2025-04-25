package com.tracking.model;

import com.tracking.validation.KebabCaseSlug;
import com.tracking.validation.ValidCountryCode;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@KebabCaseSlug
public class TrackingNumberRequest {
    @NotBlank(message = "Origin country ID is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Origin country ID must be a valid ISO 3166-1 alpha-2 code")
    @ValidCountryCode(message = "Origin country code not supported")
    private final String originCountryId;

    @NotBlank(message = "Destination country ID is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Destination country ID must be a valid ISO 3166-1 alpha-2 code")
    @ValidCountryCode(message = "Destination country code not supported")
    private final String destinationCountryId;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.001", message = "Weight must be at least 0.001 kg")
    @DecimalMax(value = "999.999", message = "Weight must be at most 999.999 kg")
    private final Double weight;

    @NotBlank(message = "Created at timestamp is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([+-]\\d{2}:\\d{2}|Z)$", 
             message = "Created at must be in RFC 3339 format (e.g., 2018-11-20T19:29:32+08:00)")
    private final String createdAt;

    @NotBlank(message = "Customer ID is required")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", 
             message = "Customer ID must be a valid UUID")
    private final String customerId;

    @NotBlank(message = "Customer name is required")
    @Size(min = 1, max = 100, message = "Customer name must be between 1 and 100 characters")
    private final String customerName;

    @NotBlank(message = "Customer slug is required")
    @Size(min = 1, max = 100, message = "Customer slug must be between 1 and 100 characters")
    private final String customerSlug;
} 