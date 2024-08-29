package com.pizzaDeliveryProject.delivery.mappers;

import com.pizzaDeliveryProject.delivery.auth.RegisterRequest;
import com.pizzaDeliveryProject.delivery.dto.UserDTO;
import com.pizzaDeliveryProject.delivery.dto.UsersDTO;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.models.user.Role;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    public UsersDTO userToUserDTO(User user) {
        if (user == null) return null;
        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole().name() : "USER");
        return dto;
    }

    public User userDTOToUser(UsersDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole() != null ? Role.valueOf(dto.getRole()) : Role.CLIENT);
        return user;
    }
}
