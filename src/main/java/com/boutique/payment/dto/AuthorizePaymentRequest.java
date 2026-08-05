package com.boutique.payment.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;
public record AuthorizePaymentRequest(
 @NotNull UUID orderId,
 @NotBlank @Size(max=100) String idempotencyKey,
 @NotNull @DecimalMin("0.01") BigDecimal amount,
 @NotBlank @Pattern(regexp="[A-Z]{3}") String currency,
 @NotBlank @Size(min=4,max=4) String cardLast4
) {}
