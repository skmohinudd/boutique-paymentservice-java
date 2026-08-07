package com.boutique.payment.repository;
import com.boutique.payment.entity.PaymentRefund;import org.springframework.data.jpa.repository.JpaRepository;import java.util.UUID;
public interface PaymentRefundRepository extends JpaRepository<PaymentRefund,UUID>{boolean existsByPaymentId(UUID paymentId);}
