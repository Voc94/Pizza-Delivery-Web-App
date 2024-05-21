package com.pizzaDeliveryProject.delivery.service;
import com.pizzaDeliveryProject.delivery.dto.UserDTO;

import java.util.List;

public interface AdminService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();
}
