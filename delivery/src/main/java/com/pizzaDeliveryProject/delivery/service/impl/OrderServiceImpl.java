package com.pizzaDeliveryProject.delivery.service.impl;

import com.pizzaDeliveryProject.delivery.dto.OrderDTO;
import com.pizzaDeliveryProject.delivery.models.Order;
import com.pizzaDeliveryProject.delivery.repository.OrderRepository;
import com.pizzaDeliveryProject.delivery.repository.PizzaRepository;
import com.pizzaDeliveryProject.delivery.repository.RestaurantRepository;
import com.pizzaDeliveryProject.delivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private PizzaRepository pizzaRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setRestaurantId(orderDTO.getRestaurantId());
        order.setPizzaId(orderDTO.getPizzaId());
        order.setOrderStatus(Order.OrderStatus.PLACED);

        Order savedOrder = orderRepository.save(order);
        OrderDTO orderdto = convertToDTO(savedOrder);
        orderdto.setRestaurantName(restaurantRepository.findById(savedOrder.getRestaurantId()).get().getName());
        orderdto.setPizzaName(pizzaRepository.findById(savedOrder.getPizzaId()).get().getName());

        return orderdto;
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        Order savedOrder = orderRepository.save(order);
        OrderDTO orderDTO = convertToDTO(savedOrder);
        orderDTO.setRestaurantName(restaurantRepository.findById(savedOrder.getRestaurantId()).get().getName());
        orderDTO.setPizzaName(pizzaRepository.findById(savedOrder.getPizzaId()).get().getName());
        return orderDTO;
    }

    @Override
    public List<OrderDTO> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(order -> {
                    OrderDTO dto = convertToDTO(order);
                    dto.setRestaurantName(restaurantRepository.findById(order.getRestaurantId()).get().getName());
                    dto.setPizzaName(pizzaRepository.findById(order.getPizzaId()).get().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findAllByRestaurantId(Long restaurantId) {
        return orderRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(order -> {
                    OrderDTO dto = convertToDTO(order);
                    dto.setRestaurantName(restaurantRepository.findById(order.getRestaurantId()).get().getName());
                    dto.setPizzaName(pizzaRepository.findById(order.getPizzaId()).get().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setPizzaId(order.getPizzaId());
        dto.setOrderStatus(order.getOrderStatus().toString());
        return dto;
    }
}
