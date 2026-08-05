package com.boutique.payment.dto;
import jakarta.validation.constraints.*;import java.math.BigDecimal;
public record RefundPaymentRequest(@NotNull @DecimalMin("0.01") BigDecimal amount,@NotBlank String reason){}
