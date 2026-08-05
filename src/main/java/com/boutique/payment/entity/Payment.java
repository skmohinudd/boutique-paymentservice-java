package com.boutique.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="payments")
public class Payment {
 @Id private UUID id;
 @Column(name="order_id", nullable=false) private UUID orderId;
 @Column(name="idempotency_key", nullable=false, unique=true, length=100) private String idempotencyKey;
 @Column(nullable=false, precision=19, scale=2) private BigDecimal amount;
 @Column(nullable=false, length=3) private String currency;
 @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private PaymentStatus status;
 @Column(name="provider_reference", nullable=false, length=100) private String providerReference;
 @Column(name="created_at", nullable=false) private Instant createdAt;

 protected Payment() {}
 public Payment(UUID orderId, String key, BigDecimal amount, String currency, PaymentStatus status) {
   this.id=UUID.randomUUID(); this.orderId=orderId; this.idempotencyKey=key; this.amount=amount;
   this.currency=currency; this.status=status; this.providerReference="SIM-"+UUID.randomUUID();
   this.createdAt=Instant.now();
 }
 public UUID getId(){return id;} public UUID getOrderId(){return orderId;}
 public BigDecimal getAmount(){return amount;} public String getCurrency(){return currency;}
 public PaymentStatus getStatus(){return status;} public String getProviderReference(){return providerReference;}
 public Instant getCreatedAt(){return createdAt;}
}
