package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.entity.Order;
import com.example.mall.service.IOrderService;
import com.example.mall.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理", description = "订单的创建、查询、详情、状态更新接口")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "创建订单", description = "根据用户ID和收货地址创建新订单")
    @PostMapping("/create")
    public Result<Order> createOrder(@Parameter(description = "用户ID") @RequestParam Long userId,
                                      @Parameter(description = "收货地址") @RequestParam String shippingAddress) {
        try {
            Order order = orderService.createOrder(userId, shippingAddress);
            return Result.success("下单成功", order);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @Operation(summary = "获取用户订单列表", description = "根据用户ID获取该用户的所有订单")
    @GetMapping("/{userId}")
    public Result<List<Order>> getUserOrders(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详情（含订单项）")
    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(@Parameter(description = "订单ID") @PathVariable Long orderId) {
        return Result.success(orderService.getOrderWithItems(orderId));
    }

    @Operation(summary = "更新订单状态", description = "根据订单ID更新订单状态：0待支付，1已支付，2已发货，3已完成，4已取消")
    @PutMapping("/{orderId}/status")
    public Result<Void> updateOrderStatus(@Parameter(description = "订单ID") @PathVariable Long orderId,
                                          @Parameter(description = "订单状态：0待支付，1已支付，2已发货，3已完成，4已取消") @RequestParam Integer status) {
        orderService.updateOrderStatus(orderId, status);
        return Result.success("状态更新成功", null);
    }

}
