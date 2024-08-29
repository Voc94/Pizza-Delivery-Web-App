package com.pizzaDeliveryProject.delivery.service.impl;

import com.pizzaDeliveryProject.delivery.dto.RestaurantDTO;
import com.pizzaDeliveryProject.delivery.mappers.RestaurantMapper;
import com.pizzaDeliveryProject.delivery.models.restaurant.Pizza;
import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import com.pizzaDeliveryProject.delivery.models.user.Role;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.repository.PizzaRepository;
import com.pizzaDeliveryProject.delivery.repository.RestaurantRepository;
import com.pizzaDeliveryProject.delivery.repository.UserRepository;
import com.pizzaDeliveryProject.delivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::RestaurantToRestaurantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDTO getRestaurantById(Long id) {
        return RestaurantMapper.RestaurantToRestaurantDTO(restaurantRepository.findById(id).orElse(null));
    }
    public Pizza addPizza(Long managerId, Pizza pizza) {
        // Find the restaurant managed by this manager
        Restaurant restaurant = restaurantRepository.findByManagerId(managerId);
        pizza.setRestaurant(restaurant);
        return pizzaRepository.save(pizza);
    }
    @Override
    public RestaurantDTO saveRestaurant(String name, String description, Long managerId, MultipartFile file) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setManager((User)userRepository.findById(managerId).orElse(null));
        if (file != null) {
            try {
                restaurant.setPhoto(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        restaurant = restaurantRepository.save(restaurant);
        return RestaurantMapper.RestaurantToRestaurantDTO(restaurant);
    }

    @Override
    public RestaurantDTO updateRestaurant(Long id, String name, String description, Long managerId, MultipartFile file) {
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElse(null);
        if (existingRestaurant != null) {
            existingRestaurant.setName(name);
            existingRestaurant.setDescription(description);
            existingRestaurant.setManager((User)userRepository.findById(managerId).orElse(null));
            if (file != null) {
                try {
                    existingRestaurant.setPhoto(file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
    @Override
    public RestaurantDTO getRestaurantByManager(Long managerId) {
        Restaurant restaurant = restaurantRepository.findByManagerId(managerId);
        return convertToDTO(restaurant);
    }
    @Override
    public RestaurantDTO getRestaurantByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Restaurant> restaurant = restaurantRepository.findById(user.get().getRestaurant().getId());
        return convertToDTO(restaurant.orElse(null));
    }

@Override
    public RestaurantDTO updateRestaurant(Long managerId, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.findByManagerId(managerId);

        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setPhoto(restaurantDTO.getPhoto());

        return convertToDTO(restaurantRepository.save(restaurant));
    }
@Override
    public User addEmployee(Long managerId, User employee) {
        Restaurant restaurant = restaurantRepository.findByManagerId(managerId);

        employee.setRestaurant(restaurant);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setRole(Role.STAFF);
        employee.setId(0L);
        employee.setEmail(employee.getEmail());
        return userRepository.save(employee);
    }
    @Override
    public List<User> getEmployeesByManager(Long managerId) {
        Restaurant restaurant = restaurantRepository.findByManagerId(managerId);
        return userRepository.findByRestaurantId(restaurant.getId());
    }
    @Override
    public Long getRestaurantIdByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getRestaurant() != null) {
            return user.getRestaurant().getId();
        }
        return null;
    }


    @Override
public RestaurantDTO convertToDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setPhoto(restaurant.getPhoto());
        return dto;
    }
}
