package com.a2z.backend.controller;

import com.a2z.backend.dto.PaymentRequest;
import com.a2z.backend.entity.Payment;
import com.a2z.backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<Payment> processPayment(
            @PathVariable Long orderId,
            @RequestBody PaymentRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Payment payment = paymentService.processPayment(
                email,
                orderId,
                request.getPaymentMethod(),
                request.getIdempotencyKey()
        );

        return ResponseEntity.ok(payment);
    }
}