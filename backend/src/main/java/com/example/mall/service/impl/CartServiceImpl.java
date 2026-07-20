package com.example.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mall.entity.Cart;
import com.example.mall.entity.Product;
import com.example.mall.mapper.CartMapper;
import com.example.mall.mapper.ProductMapper;
import com.example.mall.service.ICartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final ProductMapper productMapper;

    public CartServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public List<Map<String, Object>> getCartItems(Long userId) {
        List<Cart> carts = baseMapper.selectList(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));

        return carts.stream().map(cart -> {
            Map<String, Object> item = new HashMap<>();
            Product product = productMapper.selectById(cart.getProductId());
            item.put("cartId", cart.getId());
            item.put("productId", product.getId());
            item.put("productName", product.getName());
            item.put("productPrice", product.getPrice());
            item.put("productImage", product.getImage());
            item.put("quantity", cart.getQuantity());
            item.put("totalPrice", product.getPrice().multiply(java.math.BigDecimal.valueOf(cart.getQuantity())));
            return item;
        }).toList();
    }

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        Cart existingCart = baseMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId));

        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            baseMapper.updateById(existingCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            baseMapper.insert(cart);
        }
    }

    @Override
    @Transactional
    public void updateCartItem(Long userId, Long productId, Integer quantity) {
        Cart cart = baseMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId));

        if (cart != null) {
            cart.setQuantity(quantity);
            baseMapper.updateById(cart);
        }
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        baseMapper.delete(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId));
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        baseMapper.delete(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));
    }

}
