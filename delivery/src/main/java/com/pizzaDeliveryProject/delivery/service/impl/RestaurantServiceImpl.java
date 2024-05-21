package com.pizzaDeliveryProject.delivery.service.impl;

import com.pizzaDeliveryProject.delivery.dto.RestaurantDTO;
import com.pizzaDeliveryProject.delivery.mappers.RestaurantMapper;
import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import com.pizzaDeliveryProject.delivery.repository.RestaurantRepository;
import com.pizzaDeliveryProject.delivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::RestaurantToRestaurantDTO)  // Make sure this is the correct method name
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDTO getRestaurantById(Long id) {
        return RestaurantMapper.RestaurantToRestaurantDTO(restaurantRepository.findById(id).orElse(null));
    }

    @Override
    public RestaurantDTO saveRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = RestaurantMapper.RestaurantDTOToRestaurant(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return RestaurantMapper.RestaurantToRestaurantDTO(restaurant);
    }

    @Override
    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO) {
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElse(null);
        if (existingRestaurant != null) {
            existingRestaurant.setName(restaurantDTO.getName());
            existingRestaurant.setDescription(restaurantDTO.getDescription());
            existingRestaurant.setPhoto(restaurantDTO.getPhoto());
            existingRestaurant = restaurantRepository.save(existingRestaurant);
            return RestaurantMapper.RestaurantToRestaurantDTO(existingRestaurant);
        }
        return null;
    }

    @Override
    public List<RestaurantDTO> searchRestaurants(String query) {
        return restaurantRepository.findByNameContainingIgnoreCase(query).stream()
                .map(RestaurantMapper::RestaurantToRestaurantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
