package com.a2z.backend.controller;

import com.a2z.backend.entity.Order;
import com.a2z.backend.repository.OrderRepository;
import com.a2z.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService,
                           OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    /*
     * ===============================
     * CHECKOUT (Cart → Order)
     * ===============================
     *
     * Takes current logged-in user's cart
     * → Creates Order
     * → Clears cart
     */
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(Authentication authentication) {

        String email = authentication.getName();

        Order order = orderService.checkout(email);

        return ResponseEntity.ok(order);
    }

    /*
     * ===============================
     * GET MY ORDERS
     * ===============================
     *
     * Returns all orders of logged-in user
     */
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {

        String email = authentication.getName();

        List<Order> orders =
                orderRepository.findByUserEmail(email);

        return ResponseEntity.ok(orders);
    }

    /*
     * ===============================
     * GET ORDER BY ID
     * ===============================
     *
     * Only allows user to view their own order
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        String email = authentication.getName();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        return ResponseEntity.ok(order);
    }
}