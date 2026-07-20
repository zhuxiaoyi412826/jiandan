package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.service.ICartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public Result<List<Map<String, Object>>> getCart(@PathVariable Long userId) {
        return Result.success(cartService.getCartItems(userId));
    }

    @PostMapping("/add")
    public Result<Void> addToCart(@RequestParam Long userId, @RequestParam Long productId,
                                   @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addToCart(userId, productId, quantity);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result<Void> updateCartItem(@RequestParam Long userId, @RequestParam Long productId,
                                        @RequestParam Integer quantity) {
        cartService.updateCartItem(userId, productId, quantity);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/remove")
    public Result<Void> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        return Result.success("移除成功", null);
    }

    @DeleteMapping("/clear/{userId}")
    public Result<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return Result.success("清空成功", null);
    }

}
