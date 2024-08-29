package com.pizzaDeliveryProject.delivery.dto;


import com.pizzaDeliveryProject.delivery.models.user.Role;
import lombok.Data;

@Data
public class UsersDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role; // Role as a String to avoid enum issues
}
