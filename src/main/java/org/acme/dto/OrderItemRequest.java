package org.acme.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotBlank String productName,
        @Positive int quantity,
        @DecimalMin("0.01") BigDecimal price
) {
}
