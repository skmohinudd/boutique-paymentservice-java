package com.boutique.payment.controller;
import com.boutique.payment.dto.*;
import com.boutique.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
 private final PaymentService service;
 public PaymentController(PaymentService service){this.service=service;}
 @PostMapping("/authorize")
 ResponseEntity<PaymentResponse> authorize(@Valid @RequestBody AuthorizePaymentRequest request){
   PaymentResponse response=service.authorize(request);
   return ResponseEntity.status(response.status().name().equals("AUTHORIZED") ? HttpStatus.CREATED : HttpStatus.PAYMENT_REQUIRED).body(response);
 }
}
