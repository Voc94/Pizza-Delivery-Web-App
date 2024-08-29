package com.pizzaDeliveryProject.delivery.repository;

import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import com.pizzaDeliveryProject.delivery.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findByRestaurantId(Long id);
}
