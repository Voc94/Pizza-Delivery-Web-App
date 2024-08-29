package com.pizzaDeliveryProject.delivery.dto;

import com.pizzaDeliveryProject.delivery.models.Order.OrderStatus;
import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private Long pizzaId;
    private String restaurantName;
    private String pizzaName;
    private String orderStatus;
}
