package com.a2z.backend.service;

import com.a2z.backend.entity.Payment;

public interface PaymentService {

    Payment processPayment(String email,
                           Long orderId,
                           String paymentMethod,
                           String idempotencyKey);
}