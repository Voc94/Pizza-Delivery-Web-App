package com.pizzaDeliveryProject.delivery.mappers;

import com.pizzaDeliveryProject.delivery.dto.UserDTO;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.models.user.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO userToUserDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole().name() : "USER");
        dto.setRestaurant(user.getRestaurant());
        return dto;
    }

    public User userDTOToUser(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRestaurant(dto.getRestaurant());
        user.setRole(dto.getRole() != null ? Role.valueOf(dto.getRole()) : Role.CLIENT);
        return user;
    }
}
