package com.a2z.backend.service.impl;

import com.a2z.backend.entity.*;
import com.a2z.backend.repository.*;
import com.a2z.backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order checkout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order(user, 0.0);

        double totalAmount = 0.0;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: "
                        + product.getName());
            }

            // Reduce stock
            product.setStockQuantity(
                    product.getStockQuantity() - cartItem.getQuantity()
            );

            // Create order item (snapshot)
            OrderItem orderItem = new OrderItem(
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );

            order.addItem(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        // Clear cart
        cart.getItems().clear();

        return orderRepository.save(order);
    }
}