package com.pizzaDeliveryProject.delivery.service;

import com.pizzaDeliveryProject.delivery.dto.OrderDTO;
import com.pizzaDeliveryProject.delivery.models.Order;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status);
    List<OrderDTO> findAllByUserId(Long userId);
    List<OrderDTO> findAllByRestaurantId(Long restaurantId);
}
