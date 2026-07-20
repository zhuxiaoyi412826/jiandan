package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "购物车管理", description = "购物车的查询、添加、更新、移除、清空接口")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "获取用户购物车", description = "根据用户ID获取购物车商品列表")
    @GetMapping("/{userId}")
    public Result<List<Map<String, Object>>> getCart(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(cartService.getCartItems(userId));
    }

    @Operation(summary = "添加商品到购物车", description = "将指定商品添加到用户购物车")
    @PostMapping("/add")
    public Result<Void> addToCart(@Parameter(description = "用户ID") @RequestParam Long userId,
                                   @Parameter(description = "商品ID") @RequestParam Long productId,
                                   @Parameter(description = "数量，默认1") @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addToCart(userId, productId, quantity);
        return Result.success("添加成功", null);
    }

    @Operation(summary = "更新购物车商品数量", description = "更新用户购物车中指定商品的数量")
    @PutMapping("/update")
    public Result<Void> updateCartItem(@Parameter(description = "用户ID") @RequestParam Long userId,
                                        @Parameter(description = "商品ID") @RequestParam Long productId,
                                        @Parameter(description = "新的数量") @RequestParam Integer quantity) {
        cartService.updateCartItem(userId, productId, quantity);
        return Result.success("更新成功", null);
    }

    @Operation(summary = "移除购物车商品", description = "从用户购物车中移除指定商品")
    @DeleteMapping("/remove")
    public Result<Void> removeFromCart(@Parameter(description = "用户ID") @RequestParam Long userId,
                                       @Parameter(description = "商品ID") @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        return Result.success("移除成功", null);
    }

    @Operation(summary = "清空用户购物车", description = "清空指定用户的整个购物车")
    @DeleteMapping("/clear/{userId}")
    public Result<Void> clearCart(@Parameter(description = "用户ID") @PathVariable Long userId) {
        cartService.clearCart(userId);
        return Result.success("清空成功", null);
    }

}
