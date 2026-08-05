package com.boutique.payment.dto;
import com.boutique.payment.entity.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
public record PaymentResponse(UUID id, UUID orderId, BigDecimal amount, String currency,
 PaymentStatus status, String providerReference, Instant createdAt) {}
