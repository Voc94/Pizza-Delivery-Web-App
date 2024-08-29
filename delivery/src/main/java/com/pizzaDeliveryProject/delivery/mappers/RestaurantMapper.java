package com.pizzaDeliveryProject.delivery.mappers;

import com.pizzaDeliveryProject.delivery.dto.RestaurantDTO;
import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;

public class RestaurantMapper {

    public static RestaurantDTO RestaurantToRestaurantDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setPhoto(restaurant.getPhoto());  // Assuming photo is a byte array
        dto.setPizzaList(PizzaMapper.pizzasToPizzasDTO(restaurant.getPizzaList()));  // Assuming method to convert list of Pizzas to PizzaDTOs
        return dto;
    }

    public static Restaurant RestaurantDTOToRestaurant(RestaurantDTO dto) {
        if (dto == null) {
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(dto.getId());
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setPhoto(dto.getPhoto());  // Assuming photo is a byte array
        restaurant.setPizzaList(PizzaMapper.pizzaDTOsToPizzas(dto.getPizzaList()));  // Convert list of PizzaDTO to list of Pizza
        return restaurant;
    }
}
