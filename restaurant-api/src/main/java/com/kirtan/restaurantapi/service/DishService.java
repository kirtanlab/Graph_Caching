package com.kirtan.restaurantapi.service;

import com.kirtan.restaurantapi.model.Dish;

public interface DishService {

    Dish saveDish(Dish dish);

    void deleteDish(Dish dish);
}
