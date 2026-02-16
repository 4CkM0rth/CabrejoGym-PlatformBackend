package com.cabrejogym.platform_ecommerce.application.controller;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AddToCartRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateCartItemRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CartDTO;
import com.cabrejogym.platform_ecommerce.application.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartDTO getCart(Authentication auth, HttpSession session) {
        String email = auth != null ? auth.getName() : null;
        String sessionId = session.getId();
        return cartService.getMyCart(email, sessionId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDTO addToCart(@Valid @RequestBody AddToCartRequest request,
                             Authentication auth,
                             HttpSession session) {
        String email = auth != null ? auth.getName() : null;
        String sessionId = session.getId();
        return cartService.addToCart(email, sessionId, request);
    }

    @PutMapping("/items/{itemId}")
    public CartDTO updateCartItem(@PathVariable Long itemId,
                                  @Valid @RequestBody UpdateCartItemRequest request,
                                  Authentication auth,
                                  HttpSession session) {
        String email = auth != null ? auth.getName() : null;
        String sessionId = session.getId();
        return cartService.updateCartItem(email, sessionId, itemId, request);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromCart(@PathVariable Long itemId,
                               Authentication auth,
                               HttpSession session) {
        String email = auth != null ? auth.getName() : null;
        String sessionId = session.getId();
        cartService.removeFromCart(email, sessionId, itemId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Authentication auth, HttpSession session) {
        String email = auth != null ? auth.getName() : null;
        String sessionId = session.getId();
        cartService.clearCart(email, sessionId);
    }

    @PostMapping("/merge")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void mergeCart(Authentication auth, HttpSession session) {
        if (auth == null) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        cartService.mergeGuestCartToUser(auth.getName(), session.getId());
    }
}
