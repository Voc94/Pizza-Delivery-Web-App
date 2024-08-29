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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

@SpringBootApplication
public class DeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder, RestaurantRepository restaurantRepository, PizzaRepository pizzaRepository) {
		return args -> {
			createAdminUser(userRepository, passwordEncoder);
			createUser(userRepository, passwordEncoder);

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
					// Existing pizzerias
					{"manager1@restaurant.com", "The Pie Pizzeria", "Urban style pizza with unique combinations", "src/main/resources/photos/djorno.jpg"},
					{"manager2@restaurant.com", "Luigi's Restaurant", "Traditional Italian cuisine with a modern twist", "src/main/resources/photos/2.jpg"},
					{"manager3@restaurant.com", "Nom Pizza Bar", "Fresh pizza made daily with organic ingredients", "src/main/resources/photos/3.jpg"},
					{"manager4@restaurant.com", "JJoint", "Gourmet pizza and craft beers", "src/main/resources/photos/4.jpg"},
					{"manager5@restaurant.com", "Label's Pizza", "Authentic Italian pizza and wine", "src/main/resources/photos/5.jpg"},

					// New pizzerias
					{"manager6@restaurant.com", "Bella Italia", "Classic Italian pizza with fresh ingredients", "src/main/resources/photos/6.jpg"},
					{"manager7@restaurant.com", "Crust & Craft", "Artisanal pizzas with a variety of crust options", "src/main/resources/photos/7.jpg"},
					{"manager8@restaurant.com", "Pizza Haven", "A haven for pizza lovers with a wide range of toppings", "src/main/resources/photos/8.jpg"},
					{"manager9@restaurant.com", "Wood Fired Wonders", "Authentic wood-fired pizzas with unique toppings", "src/main/resources/photos/9.jpg"},
					{"manager10@restaurant.com", "The Dough Company", "Handcrafted pizzas made from scratch", "src/main/resources/photos/10.jpg"},
					{"manager11@restaurant.com", "Sicilian Slice", "Traditional Sicilian pizza with a rich flavor", "src/main/resources/photos/11.jpg"},
					{"manager12@restaurant.com", "Gourmet Pizza Co.", "High-end pizzas with gourmet ingredients", "src/main/resources/photos/12.jpg"},
					{"manager13@restaurant.com", "The Pizza Oven", "Pizzas baked to perfection in a traditional oven", "src/main/resources/photos/13.jpg"},
					{"manager14@restaurant.com", "Cheese & Tomato", "Delicious pizzas with a variety of cheese options", "src/main/resources/photos/14.jpg"},
					{"manager15@restaurant.com", "The Crusty Corner", "Crispy, flavorful pizzas with unique crust options", "src/main/resources/photos/15.jpg"}
			};

			for (String[] data : restaurantData) {
				String managerEmail = data[0];
				String restaurantName = data[1];
				String restaurantDescription = data[2];
				String photoPath = data[3];

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

				try {
					byte[] photoBytes = Files.readAllBytes(Paths.get(photoPath));
					Restaurant restaurant = new Restaurant(null, restaurantName, restaurantDescription, photoBytes, null, manager);
					restaurant = restaurantRepository.saveAndFlush(restaurant);

					Pizza pizza1 = new Pizza(null, "Classic", "A timeless classic with cheese and tomato sauce.", 9.99f, restaurant);
					Pizza pizza2 = new Pizza(null, "Specialty", "Our special with extra toppings and secret sauce.", 14.99f, restaurant);
					pizzaRepository.save(pizza1);
					pizzaRepository.save(pizza2);

					restaurant.setPizzaList(Arrays.asList(pizza1, pizza2));
					restaurantRepository.save(restaurant);
					System.out.println("Restaurant created: " + restaurant.getName() + " with manager " + manager.getUsername());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Failed to read the image file for restaurant: " + restaurantName);
				}
			}
		};
	}


	private void createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
	}

	private void createUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		String userEmail = "vasi@gmail.com";
		Optional<User> existingUser = userRepository.findByEmail(userEmail);
		if (existingUser.isEmpty()) {
			User user = User.builder()
					.email(userEmail)
					.username("Vasi")
					.password(passwordEncoder.encode("user1234"))
					.role(Role.CLIENT)
					.build();
			userRepository.save(user);
			System.out.println("User created");
		} else {
			System.out.println("User already exists");
		}
	}

	private void createRestaurantWithPizzas(User manager, RestaurantRepository restaurantRepository, PizzaRepository pizzaRepository) {
		try {
			byte[] photoBytes = Files.readAllBytes(Paths.get("src/main/resources/photos/1.jpg"));

			Restaurant restaurant = new Restaurant(null, "Giovanni's Pizzeria", "The best Italian pizza in town.", photoBytes, null, manager);
			restaurant = restaurantRepository.saveAndFlush(restaurant);

			Pizza pizza1 = new Pizza(null, "Margherita", "Classic Margherita with fresh mozzarella and basil.", 10.99f, restaurant);
			Pizza pizza2 = new Pizza(null, "Pepperoni", "Loaded with pepperoni and extra cheese.", 12.99f, restaurant);
			pizzaRepository.save(pizza1);
			pizzaRepository.save(pizza2);

			restaurant.setPizzaList(Arrays.asList(pizza1, pizza2));
			restaurantRepository.save(restaurant);

			System.out.println("Restaurant with two pizzas created for the manager");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to read the image file");
		}
	}
}
