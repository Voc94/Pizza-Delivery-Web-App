package com.pizzaDeliveryProject.delivery;

import com.pizzaDeliveryProject.delivery.models.restaurant.Pizza;
import com.pizzaDeliveryProject.delivery.models.restaurant.Restaurant;
import com.pizzaDeliveryProject.delivery.models.user.Role;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.repository.PizzaRepository;
import com.pizzaDeliveryProject.delivery.repository.RestaurantRepository;
import com.pizzaDeliveryProject.delivery.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

@SpringBootApplication
public class DeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}
	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder, RestaurantRepository restaurantRepository, PizzaRepository pizzaRepository) {
		return args -> {
			String adminEmail = "admin@admin.com";
			Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);
			if (existingAdmin.isEmpty()) {
				User adminUser = User.builder()
						.email(adminEmail)
						.username("adminUser")
						.password(passwordEncoder.encode("admin1234"))
						.role(Role.ADMIN)
						.build();
				userRepository.save(adminUser);
				System.out.println("Admin user created");
			} else {
				System.out.println("Admin user already exists");
			}

			String userEmail = "vasi@gmail.com";
			Optional<User> existingUser = userRepository.findByEmail(userEmail);
			if (existingUser.isEmpty()) {
				User user = User.builder()
						.email(userEmail)
						.username("Vasi")
						.password(passwordEncoder.encode("user1234"))
						.role(Role.USER)
						.build();
				userRepository.save(user);
				System.out.println("User created");
			} else {
				System.out.println("User already exists");
			}

			userRepository.findByEmail("manager@restaurant.com").ifPresentOrElse(
					manager -> System.out.println("Restaurant Manager already exists"),
					() -> {
						User manager = User.builder()
								.email("manager@restaurant.com")
								.username("RestaurantManager")
								.password(passwordEncoder.encode("manager1234"))
								.role(Role.RESTAURANT_MANAGER)
								.build();
						userRepository.save(manager);

						createRestaurantWithPizzas(manager, restaurantRepository, pizzaRepository);
					}
			);
			String[][] restaurantData = {
					{"manager1@restaurant.com", "The Pie Pizzeria", "Urban style pizza with unique combinations"},
					{"manager2@restaurant.com", "Luigi's Restaurant", "Traditional Italian cuisine with a modern twist"},
					{"manager3@restaurant.com", "Pasta Bar", "Fresh pasta made daily with organic ingredients"},
					{"manager4@restaurant.com", "Burger Joint", "Gourmet burgers and craft beers"},
					{"manager5@restaurant.com", "Sushi House", "Authentic Japanese sushi and sashimi"}
			};

			for (String[] data : restaurantData) {
				String managerEmail = data[0];
				String restaurantName = data[1];
				String restaurantDescription = data[2];

				User manager = userRepository.findByEmail(managerEmail).orElseGet(() -> {
					User newUser = User.builder()
							.email(managerEmail)
							.username(managerEmail.split("@")[0])
							.password(passwordEncoder.encode("password1234"))
							.role(Role.RESTAURANT_MANAGER)
							.build();
					userRepository.save(newUser);
					System.out.println("New restaurant manager created: " + newUser.getUsername());
					return newUser;
				});


				Restaurant restaurant = new Restaurant(null, restaurantName, restaurantDescription,null, null, manager);
				restaurant = restaurantRepository.saveAndFlush(restaurant);

				Pizza pizza1 = new Pizza(null, "Classic", "A timeless classic with cheese and tomato sauce.", 9.99f, restaurant);
				Pizza pizza2 = new Pizza(null, "Specialty", "Our special with extra toppings and secret sauce.", 14.99f, restaurant);
				pizzaRepository.save(pizza1);
				pizzaRepository.save(pizza2);


				restaurant.setPizzaList(Arrays.asList(pizza1, pizza2));
				restaurantRepository.save(restaurant);
				System.out.println("Restaurant created: " + restaurant.getName() + " with manager " + manager.getUsername());
			}
		};
	}
	private void createRestaurantWithPizzas(User manager, RestaurantRepository restaurantRepository, PizzaRepository pizzaRepository) {

		Restaurant restaurant = new Restaurant(null, "Giovanni's Pizzeria", "The best Italian pizza in town.", null,null, manager);
		restaurant = restaurantRepository.saveAndFlush(restaurant);  // save and immediately flush to ensure it's managed


		Pizza pizza1 = new Pizza(null, "Margherita", "Classic Margherita with fresh mozzarella and basil.", 10.99f, restaurant);
		Pizza pizza2 = new Pizza(null, "Pepperoni", "Loaded with pepperoni and extra cheese.", 12.99f, restaurant);
		pizzaRepository.save(pizza1);
		pizzaRepository.save(pizza2);


		restaurant = restaurantRepository.findById(restaurant.getId()).orElse(null);
		if (restaurant != null) {
			restaurant.setPizzaList(Arrays.asList(pizza1, pizza2));
			restaurantRepository.save(restaurant);
		} else {
			System.out.println("Failed to find the restaurant after saving");
		}

		System.out.println("Restaurant with two pizzas created for the manager");
	}

}