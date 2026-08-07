package com.boutique.payment.messaging;
import com.boutique.payment.entity.PaymentOutboxEvent;import com.boutique.payment.repository.PaymentOutboxRepository;import tools.jackson.databind.ObjectMapper;import org.springframework.stereotype.Component;import java.time.Instant;import java.util.*;
@Component
public class PaymentEventPublisher{
 private final PaymentOutboxRepository outbox;private final ObjectMapper json;
 public PaymentEventPublisher(PaymentOutboxRepository outbox,ObjectMapper json){this.outbox=outbox;this.json=json;}
 public void publish(String type,UUID paymentId,UUID orderId,String status){try{Map<String,Object>e=new LinkedHashMap<>();e.put("eventId",UUID.randomUUID());e.put("eventType",type);e.put("paymentId",paymentId);e.put("orderId",orderId);e.put("status",status);e.put("occurredAt",Instant.now());outbox.save(new PaymentOutboxEvent(orderId,type,json.writeValueAsString(e)));}catch(Exception ex){throw new IllegalStateException("Payment event outbox serialization failed",ex);}}
}
