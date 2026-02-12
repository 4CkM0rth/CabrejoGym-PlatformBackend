package com.cabrejogym.platform_ecommerce.application.service;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AddToCartRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateCartItemRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CartDTO;

public interface CartService {
    
    CartDTO getMyCart(String email, String sessionId);
    
    CartDTO addToCart(String email, String sessionId, AddToCartRequest request);
    
    CartDTO updateCartItem(String email, String sessionId, Long itemId, UpdateCartItemRequest request);
    
    CartDTO removeFromCart(String email, String sessionId, Long itemId);
    
    void clearCart(String email, String sessionId);
    
    void mergeGuestCartToUser(String email, String sessionId);
}
