package com.pizzaDeliveryProject.delivery.controllers;

import com.pizzaDeliveryProject.delivery.dto.RestaurantDTO;
import com.pizzaDeliveryProject.delivery.models.restaurant.Pizza;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.service.RestaurantService;
import com.pizzaDeliveryProject.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;

    @GetMapping("/restaurant")
    public RestaurantDTO getManagedRestaurant(@AuthenticationPrincipal User manager) {
        return restaurantService.getRestaurantByManager(manager.getId());
    }

    @PutMapping("/restaurant")
    public RestaurantDTO updateRestaurant(@AuthenticationPrincipal User manager, @RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.updateRestaurant(manager.getId(), restaurantDTO);
    }

    @PostMapping("/employee")
    public User addEmployee(@AuthenticationPrincipal User manager, @RequestBody User employee) {
        return restaurantService.addEmployee(manager.getId(), employee);
    }

    @GetMapping("/restaurant/user/{userEmail}")
    public Long getRestaurantIdByUserEmail(@PathVariable String userEmail, @AuthenticationPrincipal User user) {
        User staff = userService.findByEmail(userEmail);
        Long userId = staff.getId();
        if (user.getId().equals(userId) || user.getRole().equals("ROLE_MANAGER") || user.getRole().equals("ROLE_STAFF")) {
            return restaurantService.getRestaurantIdByUserId(userId);
        }
        throw new RuntimeException("Access Denied");
    }

}

