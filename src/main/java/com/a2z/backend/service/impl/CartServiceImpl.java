package com.a2z.backend.service.impl;

import com.a2z.backend.entity.*;
import com.a2z.backend.repository.*;
import com.a2z.backend.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // ==========================
    // Private Helper
    // ==========================

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    // ==========================
    // View Cart
    // ==========================

    @Override
    public Cart getCart(String email) {
        User user = getUserByEmail(email);
        return getOrCreateCart(user);
    }

    // ==========================
    // Add to Cart
    // ==========================

    @Override
    public Cart addToCart(String email, Long productId, Integer quantity) {

        User user = getUserByEmail(email);
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(product, quantity);
            cart.addItem(cartItem);
        }

        return cartRepository.save(cart);
    }

    // ==========================
    // Decrease Quantity
    // ==========================

    @Override
    public Cart decreaseQuantity(String email, Long productId) {

        User user = getUserByEmail(email);
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            cart.removeItem(cartItem);
        }

        return cartRepository.save(cart);
    }

    // ==========================
    // Remove Item Completely
    // ==========================

    @Override
    public Cart removeFromCart(String email, Long productId) {

        User user = getUserByEmail(email);
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        cart.removeItem(cartItem);

        return cartRepository.save(cart);
    }
}