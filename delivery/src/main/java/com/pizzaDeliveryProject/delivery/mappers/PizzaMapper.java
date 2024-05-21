package com.pizzaDeliveryProject.delivery.mappers;

import com.pizzaDeliveryProject.delivery.dto.PizzaDTO;
import com.pizzaDeliveryProject.delivery.models.restaurant.Pizza;

import java.util.List;
import java.util.stream.Collectors;

public class PizzaMapper {

    // Converts a single Pizza entity to PizzaDTO
    public static PizzaDTO pizzaToPizzaDTO(Pizza pizza) {
        if (pizza == null) {
            return null;
        }
        return new PizzaDTO(
                pizza.getId(),
                pizza.getName(),
                pizza.getDescription(),
                pizza.getPrice()
        );
    }

    // Converts a list of Pizza entities to a list of PizzaDTOs
    public static List<PizzaDTO> pizzasToPizzasDTO(List<Pizza> pizzas) {
        if (pizzas == null) {
            return null;
        }
        return pizzas.stream()
                .map(PizzaMapper::pizzaToPizzaDTO)
                .collect(Collectors.toList());
    }
    public static Pizza pizzaDTOToPizza(PizzaDTO pizzaDTO) {
        if (pizzaDTO == null) {
            return null;
        }
        Pizza pizza = new Pizza();
        pizza.setId(pizzaDTO.getId());
        pizza.setName(pizzaDTO.getName());
        pizza.setDescription(pizzaDTO.getDescription());
        pizza.setPrice(pizzaDTO.getPrice());
        return pizza;
    }

    // Converts a list of PizzaDTOs to a list of Pizzas
    public static List<Pizza> pizzaDTOsToPizzas(List<PizzaDTO> pizzaDTOs) {
        if (pizzaDTOs == null) {
            return null;
        }
        return pizzaDTOs.stream()
                .map(PizzaMapper::pizzaDTOToPizza)
                .collect(Collectors.toList());
    }
}
