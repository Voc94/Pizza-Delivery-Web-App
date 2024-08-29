package com.pizzaDeliveryProject.delivery.controllers;

import com.pizzaDeliveryProject.delivery.dto.OrderDTO;
import com.pizzaDeliveryProject.delivery.models.Order;
import com.pizzaDeliveryProject.delivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @PutMapping("/{orderId}/status")
    public OrderDTO updateOrderStatus(@PathVariable Long orderId, @RequestParam Order.OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> findAllByUserId(@PathVariable Long userId) {
        return orderService.findAllByUserId(userId);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<OrderDTO> findAllByRestaurantId(@PathVariable Long restaurantId) {
        return orderService.findAllByRestaurantId(restaurantId);
    }
}
