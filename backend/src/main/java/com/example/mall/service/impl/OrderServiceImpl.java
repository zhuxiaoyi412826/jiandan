package com.example.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mall.entity.*;
import com.example.mall.mapper.*;
import com.example.mall.service.IOrderService;
import com.example.mall.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderServiceImpl(CartMapper cartMapper, ProductMapper productMapper, OrderItemMapper orderItemMapper) {
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, String shippingAddress) {
        List<Cart> carts = cartMapper.selectList(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));

        if (carts.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : carts) {
            Product product = productMapper.selectById(cart.getProductId());
            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setShippingAddress(shippingAddress);
        baseMapper.insert(order);

        for (Cart cart : carts) {
            Product product = productMapper.selectById(cart.getProductId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItemMapper.insert(orderItem);

            product.setStock(product.getStock() - cart.getQuantity());
            productMapper.updateById(product);
        }

        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));

        return order;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt));
    }

    @Override
    public OrderVO getOrderWithItems(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        if (order == null) {
            return null;
        }
        OrderVO orderVO = new OrderVO();
        orderVO.setId(order.getId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setUserId(order.getUserId());
        orderVO.setTotalAmount(order.getTotalAmount());
        orderVO.setStatus(order.getStatus());
        orderVO.setShippingAddress(order.getShippingAddress());
        orderVO.setCreatedAt(order.getCreatedAt());
        orderVO.setUpdatedAt(order.getUpdatedAt());

        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, orderId));
        orderVO.setItems(items);

        return orderVO;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Integer status) {
        Order order = baseMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(status);
            baseMapper.updateById(order);
        }
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "ORD" + timestamp + random;
    }

}
