package com.a2z.backend.controller;

import com.a2z.backend.entity.Cart;
import com.a2z.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /*
     * Get logged-in user's email from session
     */
    private String getLoggedInUserEmail(Authentication authentication) {
        return authentication.getName();
    }

    // ==========================
    // View Cart
    // ==========================

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {

        String email = getLoggedInUserEmail(authentication);

        return ResponseEntity.ok(cartService.getCart(email));
    }

    // ==========================
    // Add to Cart
    // ==========================

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            Authentication authentication,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        String email = getLoggedInUserEmail(authentication);

        return ResponseEntity.ok(
                cartService.addToCart(email, productId, quantity)
        );
    }

    // ==========================
    // Decrease Quantity
    // ==========================

    @PutMapping("/decrease")
    public ResponseEntity<Cart> decreaseQuantity(
            Authentication authentication,
            @RequestParam Long productId) {

        String email = getLoggedInUserEmail(authentication);

        return ResponseEntity.ok(
                cartService.decreaseQuantity(email, productId)
        );
    }

    // ==========================
    // Remove Item
    // ==========================

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(
            Authentication authentication,
            @RequestParam Long productId) {

        String email = getLoggedInUserEmail(authentication);

        return ResponseEntity.ok(
                cartService.removeFromCart(email, productId)
        );
    }
}