package com.kirtan.restaurantapi.service;

import com.kirtan.restaurantapi.exception.CityNotFoundException;
import com.kirtan.restaurantapi.repository.CityRepository;
import com.kirtan.restaurantapi.model.City;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public Page<City> getCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public void deleteCity(City city) {
        cityRepository.delete(city);
    }

    @Override
    public City validateAndGetCity(UUID cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new CityNotFoundException(cityId));
    }
}
