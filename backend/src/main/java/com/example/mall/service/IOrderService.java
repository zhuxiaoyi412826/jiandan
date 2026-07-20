package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.Order;
import com.example.mall.vo.OrderVO;

import java.util.List;

public interface IOrderService extends IService<Order> {
    Order createOrder(Long userId, String shippingAddress);
    List<Order> getUserOrders(Long userId);
    OrderVO getOrderWithItems(Long orderId);
    void updateOrderStatus(Long orderId, Integer status);
}
