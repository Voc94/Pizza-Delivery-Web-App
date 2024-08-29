package com.pizzaDeliveryProject.delivery.service;

import com.pizzaDeliveryProject.delivery.dto.UserDTO;

import java.util.List;

public interface AdminService {
    UserDTO updateUser(Long userId, UserDTO userDTO);

    UserDTO createUser(UserDTO userDTO);

    void deleteUser(Long userId);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

    String exportUsersAsXml() throws Exception;
}
