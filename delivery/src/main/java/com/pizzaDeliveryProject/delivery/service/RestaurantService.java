package com.pizzaDeliveryProject.delivery.service;

import com.pizzaDeliveryProject.delivery.dto.RestaurantDTO;
import com.pizzaDeliveryProject.delivery.models.restaurant.Pizza;
import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import com.pizzaDeliveryProject.delivery.models.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDTO> getAllRestaurants();
    RestaurantDTO getRestaurantById(Long id);
    RestaurantDTO saveRestaurant(String name, String description, Long managerId, MultipartFile file);
    RestaurantDTO updateRestaurant(Long id, String name, String description, Long managerId, MultipartFile file);    void deleteRestaurant(Long id);
    List<RestaurantDTO> searchRestaurants(String query);
    RestaurantDTO getRestaurantByManager(Long managerId);

    RestaurantDTO getRestaurantByUserId(Long userId);

    RestaurantDTO updateRestaurant(Long managerId, RestaurantDTO restaurantDTO);
    User addEmployee(Long managerId, User employee);

    RestaurantDTO convertToDTO(Restaurant restaurant);

    Pizza addPizza(Long id, Pizza pizza);

    List<User> getEmployeesByManager(Long id);

    Long getRestaurantIdByUserId(Long userId);
}
