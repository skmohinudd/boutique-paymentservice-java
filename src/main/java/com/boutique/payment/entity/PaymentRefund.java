package com.boutique.payment.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.Instant;import java.util.UUID;
@Entity @Table(name="payment_refunds")
public class PaymentRefund{
 @Id private UUID id;@Column(name="payment_id",nullable=false)private UUID paymentId;@Column(nullable=false)private BigDecimal amount;
 @Column(nullable=false,length=300)private String reason;@Column(nullable=false,length=20)private String status;@Column(name="created_at",nullable=false)private Instant createdAt;
 protected PaymentRefund(){} public PaymentRefund(UUID paymentId,BigDecimal amount,String reason){this.id=UUID.randomUUID();this.paymentId=paymentId;this.amount=amount;this.reason=reason;this.status="REFUNDED";this.createdAt=Instant.now();}
}
