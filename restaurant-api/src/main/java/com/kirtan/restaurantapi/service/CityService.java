package com.kirtan.restaurantapi.service;

import com.kirtan.restaurantapi.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CityService {

    City saveCity(City city);

    Page<City> getCities(Pageable pageable);

    void deleteCity(City city);

    City validateAndGetCity(UUID cityId);
}
