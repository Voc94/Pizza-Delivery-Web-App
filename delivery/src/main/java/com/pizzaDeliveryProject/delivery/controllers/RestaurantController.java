package com.pizzaDeliveryProject.delivery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzaDeliveryProject.delivery.dto.RestaurantDTO;
import com.pizzaDeliveryProject.delivery.service.RestaurantService;
import com.pizzaDeliveryProject.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        RestaurantDTO restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getRestaurantPhoto(@PathVariable Long id) {
        RestaurantDTO restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null && restaurant.getPhoto() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(restaurant.getPhoto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public RestaurantDTO createRestaurant(@RequestParam("name") String name,
                                          @RequestParam("description") String description,
                                          @RequestParam("manager_id") Long managerId,  // Add manager_id parameter
                                          @RequestParam("file") MultipartFile file) {
        return restaurantService.saveRestaurant(name, description, managerId, file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("description") String description,
                                                          @RequestParam("manager_id") Long managerId,  // Add manager_id parameter
                                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, name, description, managerId, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDTO>> searchRestaurants(@RequestParam String query) {
        List<RestaurantDTO> filteredRestaurants = restaurantService.searchRestaurants(query);
        return ResponseEntity.ok(filteredRestaurants);
    }
}
