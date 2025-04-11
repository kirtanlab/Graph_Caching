package com.kirtan.restaurantapi.service;

import com.kirtan.restaurantapi.exception.DishNotFoundException;
import com.kirtan.restaurantapi.exception.RestaurantNotFoundException;
import com.kirtan.restaurantapi.repository.DishRepository;
import com.kirtan.restaurantapi.repository.RestaurantRepository;
import com.kirtan.restaurantapi.model.Dish;
import com.kirtan.restaurantapi.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @Override
    public Page<Restaurant> getRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Restaurant restaurant) {
        restaurant.getDishes().forEach(dishRepository::delete);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public Restaurant validateAndGetRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    @Override
    public Dish validateAndGetDish(Restaurant restaurant, UUID dishId) {
        return restaurant.getDishes()
                .stream()
                .filter(dish -> dish.getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new DishNotFoundException(dishId));
    }
}
