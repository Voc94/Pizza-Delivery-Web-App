package com.pizzaDeliveryProject.delivery.service;

import com.pizzaDeliveryProject.delivery.models.user.User;

public interface UserService {
    public void assignRestaurantToManager(Long userId, Long restaurantId);
    User findByEmail(String email);
}
