package com.a2z.backend.service.impl;

import com.a2z.backend.entity.*;
import com.a2z.backend.repository.*;
import com.a2z.backend.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(OrderRepository orderRepository,
                              PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment processPayment(String email,
                                  Long orderId,
                                  String paymentMethod,
                                  String idempotencyKey) {

        // 1️⃣ Check idempotency
        Payment existingPayment =
                paymentRepository.findByIdempotencyKey(idempotencyKey)
                        .orElse(null);

        if (existingPayment != null) {
            return existingPayment; // Return previous result
        }

        // 2️⃣ Fetch order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 3️⃣ Security check
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order already processed");
        }

        // 4️⃣ Create Payment (PENDING)
        Payment payment =
                new Payment(order, paymentMethod, idempotencyKey);

        payment = paymentRepository.save(payment);

        // 5️⃣ Simulate Payment Gateway
        boolean paymentSuccess = true; // simulation

        if (paymentSuccess) {

            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());

            order.setStatus(OrderStatus.PAID);

        } else {

            payment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }
}