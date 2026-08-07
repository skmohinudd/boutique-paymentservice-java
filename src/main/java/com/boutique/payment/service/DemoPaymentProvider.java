package com.boutique.payment.service;

import com.boutique.payment.entity.PaymentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DemoPaymentProvider {
    private final long latencyMs;
    public DemoPaymentProvider(@Value("${demo.payment.latency-ms:180}") long latencyMs) {
        this.latencyMs = Math.max(0, Math.min(latencyMs, 5000));
    }
    public Result authorize(String last4) {
        try { Thread.sleep(latencyMs); }
        catch (InterruptedException ex) { Thread.currentThread().interrupt(); throw new IllegalStateException("Payment interrupted", ex); }
        if ("0000".equals(last4)) return new Result(PaymentStatus.DECLINED, "DEMO-DECLINED", "Insufficient funds");
        if ("9999".equals(last4)) throw new IllegalStateException("Simulated payment-provider timeout");
        return new Result(PaymentStatus.AUTHORIZED, "DEMO-" + UUID.randomUUID(), null);
    }
    public record Result(PaymentStatus status, String providerReference, String failureReason) {}
}
