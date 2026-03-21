package com.a2z.backend.service;

import com.a2z.backend.entity.Cart;

public interface CartService {

    Cart getCart(String email);

    Cart addToCart(String email, Long productId, Integer quantity);

    Cart decreaseQuantity(String email, Long productId);

    Cart removeFromCart(String email, Long productId);
}