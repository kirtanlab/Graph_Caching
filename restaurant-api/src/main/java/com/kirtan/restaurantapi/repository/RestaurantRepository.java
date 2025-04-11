package com.kirtan.restaurantapi.repository;

import com.kirtan.restaurantapi.model.Restaurant;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantRepository extends Neo4jRepository<Restaurant, UUID> {
}
