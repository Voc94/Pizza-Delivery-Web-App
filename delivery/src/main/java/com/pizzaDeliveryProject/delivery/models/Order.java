package com.pizzaDeliveryProject.delivery.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonManagedReference
    private Long userId;
    private Long restaurantId;
    private Long pizzaId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public enum OrderStatus {
        PLACED,
        PREPARING,
        FINISHED,
        CANCELED
    }
}
