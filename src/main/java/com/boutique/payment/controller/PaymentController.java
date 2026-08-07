package com.boutique.payment.controller;
import com.boutique.payment.dto.*;import com.boutique.payment.service.PaymentService;import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.UUID;
@RestController @RequestMapping("/api/v1/payments")
public class PaymentController{
 private final PaymentService service;public PaymentController(PaymentService service){this.service=service;}
 @PostMapping("/authorize") ResponseEntity<PaymentResponse> authorize(@Valid @RequestBody AuthorizePaymentRequest request){PaymentResponse response=service.authorize(request);return ResponseEntity.status(response.status().name().equals("AUTHORIZED")?HttpStatus.CREATED:HttpStatus.PAYMENT_REQUIRED).body(response);}
 @PostMapping("/{paymentId}/refund") PaymentResponse refund(@PathVariable UUID paymentId,@RequestParam(required=false)String reason){return service.refund(paymentId,reason);}
 @GetMapping("/{paymentId}") PaymentResponse get(@PathVariable UUID paymentId){return service.get(paymentId);}
 @GetMapping("/demo-cards") Object demoCards(){return new Object[]{new DemoCard("4242","Visa","AUTHORIZED","Successful authorization"),new DemoCard("0000","Visa","DECLINED","Simulated insufficient funds"),new DemoCard("9999","Visa","TIMEOUT","Simulated provider timeout")};}
 record DemoCard(String last4,String brand,String outcome,String description){}
}
