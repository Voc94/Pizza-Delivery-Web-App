package com.pizzaDeliveryProject.delivery.dto;

import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String password;
    private Restaurant restaurant;

}