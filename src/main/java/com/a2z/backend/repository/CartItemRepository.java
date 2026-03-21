package com.a2z.backend.repository;

import com.a2z.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /*
     * Find cart item by cart ID and product ID.
     * This helps for:
     *  - Increment quantity
     *  - Decrement quantity
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}