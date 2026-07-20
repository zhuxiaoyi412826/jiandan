package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.Cart;

import java.util.List;
import java.util.Map;

public interface ICartService extends IService<Cart> {
    List<Map<String, Object>> getCartItems(Long userId);
    void addToCart(Long userId, Long productId, Integer quantity);
    void updateCartItem(Long userId, Long productId, Integer quantity);
    void removeFromCart(Long userId, Long productId);
    void clearCart(Long userId);
}
