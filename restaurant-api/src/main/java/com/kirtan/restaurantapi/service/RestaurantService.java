package com.kirtan.restaurantapi.service;

import com.kirtan.restaurantapi.model.Dish;
import com.kirtan.restaurantapi.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RestaurantService {

    Page<Restaurant> getRestaurants(Pageable pageable);

    Restaurant saveRestaurant(Restaurant restaurant);

    void deleteRestaurant(Restaurant restaurant);

    Restaurant validateAndGetRestaurant(UUID restaurantId);

    Dish validateAndGetDish(Restaurant restaurant, UUID dishId);
}
