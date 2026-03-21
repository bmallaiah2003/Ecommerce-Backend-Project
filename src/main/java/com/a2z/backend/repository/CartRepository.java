package com.a2z.backend.repository;

import com.a2z.backend.entity.Cart;
import com.a2z.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /*
     * Find cart by user.
     * Since one user has one cart,
     * this will return Optional<Cart>.
     */
    Optional<Cart> findByUser(User user);

    /*
     * Alternative (if needed later):
     * Find cart by user ID directly.
     */
    Optional<Cart> findByUserId(Long userId);
}