package com.kirtan.restaurantapi.rest.dto;

import java.io.Serializable;
import java.util.List;

public record RestaurantMenu(List<DishResponse> dishes) implements Serializable {
}
