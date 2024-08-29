package com.pizzaDeliveryProject.delivery.repository;

import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    Restaurant findByManagerId(long managerId);
}
