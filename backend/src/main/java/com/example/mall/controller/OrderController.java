package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.entity.Order;
import com.example.mall.service.IOrderService;
import com.example.mall.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Result<Order> createOrder(@RequestParam Long userId, @RequestParam String shippingAddress) {
        try {
            Order order = orderService.createOrder(userId, shippingAddress);
            return Result.success("下单成功", order);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderWithItems(orderId));
    }

    @PutMapping("/{orderId}/status")
    public Result<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam Integer status) {
        orderService.updateOrderStatus(orderId, status);
        return Result.success("状态更新成功", null);
    }

}
