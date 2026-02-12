package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.dtos.request.AddToCartRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.request.UpdateCartItemRequest;
import com.cabrejogym.platform_ecommerce.application.dtos.response.CartDTO;
import com.cabrejogym.platform_ecommerce.application.mapper.cart.CartMapper;
import com.cabrejogym.platform_ecommerce.application.service.CartService;
import com.cabrejogym.platform_ecommerce.domain.entity.Cart;
import com.cabrejogym.platform_ecommerce.domain.entity.CartItem;
import com.cabrejogym.platform_ecommerce.domain.entity.Product;
import com.cabrejogym.platform_ecommerce.domain.entity.User;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ConflictException;
import com.cabrejogym.platform_ecommerce.infrastructure.exceptions.ResourceNotFoundException;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.CartItemRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.CartRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.ProductRepository;
import com.cabrejogym.platform_ecommerce.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartDTO getMyCart(String email, String sessionId) {
        Cart cart = findOrCreateCart(email, sessionId);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO addToCart(String email, String sessionId, AddToCartRequest request) {
        Cart cart = findOrCreateCart(email, sessionId);
        
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.productId()));

        if (product.getStock() < request.quantity()) {
            throw new ConflictException("Stock insuficiente para el producto: " + product.getName());
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), product.getId());
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.quantity();
            
            if (product.getStock() < newQuantity) {
                throw new ConflictException("Stock insuficiente para el producto: " + product.getName());
            }
            
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.quantity());
            cart.getItems().add(newItem);
        }

        Cart saved = cartRepository.save(cart);
        return cartMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CartDTO updateCartItem(String email, String sessionId, Long itemId, UpdateCartItemRequest request) {
        Cart cart = findOrCreateCart(email, sessionId);
        
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado en el carrito"));

        Product product = item.getProduct();
        if (product.getStock() < request.quantity()) {
            throw new ConflictException("Stock insuficiente para el producto: " + product.getName());
        }

        item.setQuantity(request.quantity());
        Cart saved = cartRepository.save(cart);
        return cartMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CartDTO removeFromCart(String email, String sessionId, Long itemId) {
        Cart cart = findOrCreateCart(email, sessionId);
        
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        
        Cart saved = cartRepository.save(cart);
        return cartMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void clearCart(String email, String sessionId) {
        Cart cart = findOrCreateCart(email, sessionId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void mergeGuestCartToUser(String email, String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return;
        }

        Optional<Cart> guestCart = cartRepository.findBySessionIdWithItems(sessionId);
        if (guestCart.isEmpty() || guestCart.get().getItems().isEmpty()) {
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Optional<Cart> userCart = cartRepository.findByUserEmailWithItems(email);
        
        if (userCart.isEmpty()) {
            Cart guest = guestCart.get();
            guest.setUser(user);
            guest.setSessionId(null);
            cartRepository.save(guest);
        } else {
            Cart target = userCart.get();
            Cart source = guestCart.get();
            
            for (CartItem guestItem : source.getItems()) {
                Optional<CartItem> existing = target.getItems().stream()
                        .filter(i -> i.getProduct().getId().equals(guestItem.getProduct().getId()))
                        .findFirst();
                
                if (existing.isPresent()) {
                    CartItem item = existing.get();
                    item.setQuantity(item.getQuantity() + guestItem.getQuantity());
                } else {
                    CartItem newItem = new CartItem();
                    newItem.setCart(target);
                    newItem.setProduct(guestItem.getProduct());
                    newItem.setQuantity(guestItem.getQuantity());
                    target.getItems().add(newItem);
                }
            }
            
            cartRepository.save(target);
            cartRepository.delete(source);
        }
    }

    private Cart findOrCreateCart(String email, String sessionId) {
        if (email != null) {
            return cartRepository.findByUserEmailWithItems(email)
                    .orElseGet(() -> createCartForUser(email));
        } else if (sessionId != null) {
            return cartRepository.findBySessionIdWithItems(sessionId)
                    .orElseGet(() -> createCartForSession(sessionId));
        }
        throw new ConflictException("Se requiere email o sessionId");
    }

    private Cart createCartForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        Cart cart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }

    private Cart createCartForSession(String sessionId) {
        Cart cart = Cart.builder()
                .sessionId(sessionId)
                .build();
        return cartRepository.save(cart);
    }
}
