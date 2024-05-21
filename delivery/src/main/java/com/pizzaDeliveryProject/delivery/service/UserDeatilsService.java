package com.pizzaDeliveryProject.delivery.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDeatilsService {
    UserDetails loadUserByUsername(String username);
}
