package com.boutique.payment.service;
import com.boutique.payment.dto.*;
import com.boutique.payment.entity.*;
import com.boutique.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
 private final PaymentRepository repository;
 public PaymentService(PaymentRepository repository){this.repository=repository;}

 @Transactional
 public PaymentResponse authorize(AuthorizePaymentRequest request){
   return repository.findByIdempotencyKey(request.idempotencyKey()).map(this::map).orElseGet(() -> {
     PaymentStatus status = request.cardLast4().equals("0000") ? PaymentStatus.DECLINED : PaymentStatus.AUTHORIZED;
     return map(repository.save(new Payment(request.orderId(), request.idempotencyKey(),
       request.amount(), request.currency(), status)));
   });
 }
 private PaymentResponse map(Payment p){return new PaymentResponse(p.getId(),p.getOrderId(),p.getAmount(),
   p.getCurrency(),p.getStatus(),p.getProviderReference(),p.getCreatedAt());}
}
