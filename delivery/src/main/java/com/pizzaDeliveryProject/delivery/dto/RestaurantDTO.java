package com.pizzaDeliveryProject.delivery.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String description;
    @Lob
    private byte[] photo;
    private List<PizzaDTO> pizzaList;

}
