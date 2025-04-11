package com.kirtan.restaurantapi.mapper;

import com.kirtan.restaurantapi.model.City;
import com.kirtan.restaurantapi.rest.dto.CityResponse;
import com.kirtan.restaurantapi.rest.dto.CreateCityRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityResponse toCityResponse(City city);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    City toCity(CreateCityRequest createCityRequest);
}
