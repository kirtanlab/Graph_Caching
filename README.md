<div align="center">

<h1>Restaurant API: Spring Boot Caching with Neo4j</h1> 
   
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)
![Neo4j](https://img.shields.io/badge/Neo4j-5.26.4-blue)
![Java](https://img.shields.io/badge/Java-21%2B-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

*A sophisticated restaurant management API demonstrating advanced caching strategies with Neo4j*

</div>

## Table of Contents
- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Core Features](#core-features)
- [Cache Implementation Details](#cache-implementation-details)
- [API Endpoints](#api-endpoints)
- [Testing Strategy](#testing-strategy)
- [Development & Contribution](#development--contribution)
- [Future Enhancements](#future-enhancements)

## Project Overview

### Context & Problem Statement
Modern web applications must handle high-volume data requests efficiently while maintaining performance. For restaurant data management systems, this challenge becomes more pronounced due to:
- Frequent read operations for menu items and restaurant details
- Complex data relationships (cities → restaurants → dishes)
- Need for quick response times for end-users

### Solution
The Restaurant API implements a robust caching mechanism to address these challenges. By leveraging Spring Boot's caching abstraction with multiple cache provider options, the system significantly reduces database load and improves response times.

### Key Goals
1. Demonstrate efficient caching strategies for complex domain models
2. Showcase Spring Boot integration with Neo4j graph database
3. Implement cache invalidation patterns for related entities
4. Provide multiple caching provider options for performance comparison

## Architecture

image::documentation/Caching_PNG.png[]

The application follows a layered architecture pattern with an emphasis on separation of concerns:

### Architectural Components
| Layer | Description | Key Components |
|-------|-------------|----------------|
| **Controller Layer** | Handles HTTP requests, provides REST endpoints | `CityController`, `RestaurantController`, `RestaurantDishController` |
| **Service Layer** | Contains business logic, performs data validation | `CityService`, `RestaurantService`, `DishService` |
| **Repository Layer** | Manages data access operations with Neo4j | `CityRepository`, `RestaurantRepository`, `DishRepository` |
| **Caching Layer** | Handles cache operations for different providers | `CachingConfig`, Cache annotations |
| **Model Layer** | Defines domain entities | `City`, `Restaurant`, `Dish` |
| **Mapper Layer** | Maps between DTOs and entities | `CityMapper`, `RestaurantMapper`, `DishMapper` |

### Data Flow

[Diagram: Request-Response Flow - Shows how a request flows through the system, with caching checks at appropriate points]

1. Client sends a request to the REST controller
2. Controller checks if requested data exists in cache
3. If cached, data is returned directly without database access
4. If not cached, controller calls service layer
5. Service layer performs business logic and validation
6. Repository layer retrieves data from Neo4j database
7. Data is transformed, cached, and returned to the client

## Technology Stack

| Component | Technology | Rationale |
|-----------|------------|-----------|
| **Framework** | Spring Boot 3.4.4 | Provides comprehensive infrastructure support, reducing boilerplate code |
| **Database** | Neo4j 5.26.4 | Graph database ideally suited for modeling complex relationships between entities |
| **Caching** | Simple, Caffeine, Redis | Multiple provider options for flexibility and performance comparison |
| **API Documentation** | SpringDoc OpenAPI 2.8.6 | Self-documenting API with interactive Swagger UI |
| **Build Tool** | Maven | Industry standard build automation tool |
| **Testing** | JUnit 5, Spring Boot Test, Testcontainers | Comprehensive testing framework with containerized dependencies |
| **Mapping** | MapStruct 1.6.3 | Type-safe, high-performance object mapping |
| **Containerization** | Docker | Platform for developing, shipping, and running applications |
| **Monitoring** | Micrometer, Prometheus | Application metrics collection and monitoring |

### Technology Selection Rationale

#### Neo4j as Database
Neo4j was selected for its natural ability to model and query relationships efficiently. In the restaurant domain, relationships are central:
- Cities contain multiple restaurants
- Restaurants offer multiple dishes
- These relationships need to be traversed quickly for related-entity lookups

Unlike traditional relational databases, Neo4j's graph structure optimizes relationship traversal, making operations like "find all restaurants in a city" or "get all dishes of a restaurant" more efficient.

#### Multiple Caching Providers
The project implements three caching providers, each with distinct advantages:

1. **Simple Cache** (Default)
   - Lightweight, in-memory caching
   - Best for development and testing
   - No additional dependencies required

2. **Caffeine Cache**
   - High-performance, in-memory caching
   - Advanced features like size limits, expiration policies
   - Suitable for single-node deployments

3. **Redis Cache**
   - Distributed caching solution
   - Persistence capabilities
   - Ideal for multi-node deployments

This approach allows for:
- Performance comparison between providers
- Flexibility to choose the most appropriate solution for specific deployment needs
- Demonstration of Spring Boot's abstraction capabilities

## Getting Started

### Prerequisites

| Requirement | Version | Notes |
|-------------|---------|-------|
| Java | 21+ | JDK 21 or higher required |
| Docker | Latest | For containerization |
| jq | Latest | For the simulation script |

### Environment Setup

Start the required infrastructure services:

```bash
docker compose up -d
```

This command initializes:
- Neo4j database instance
- Redis cache instance (if using Redis provider)
- Redis Commander UI for monitoring Redis

### Running the Application

Select one of the following methods based on your preferred cache provider:

#### Using Maven

With Simple cache provider:
```bash
./mvnw clean spring-boot:run --projects restaurant-api
```

With Caffeine cache provider:
```bash
./mvnw clean spring-boot:run --projects restaurant-api -Dspring-boot.run.profiles=caffeine
```

With Redis cache provider:
```bash
./mvnw clean spring-boot:run --projects restaurant-api -Dspring-boot.run.profiles=redis
```

#### Using Docker

Build the Docker image:
```bash
./build-docker-images.sh
```

Run the container (example with Redis provider):
```bash
docker run --rm --name restaurant-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=redis \
  -e NEO4J_HOST=neo4j \
  -e REDIS_HOST=redis \
  --network=springboot-caching-neo4j_default \
  kirtan/restaurant-api:1.0.0
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | default (Simple) | Set to `redis` or `caffeine` to choose provider |
| `NEO4J_HOST` | localhost | Neo4j database host |
| `NEO4J_PORT` | 7687 | Neo4j database port |
| `REDIS_HOST` | localhost | Redis host (when using Redis provider) |
| `REDIS_PORT` | 6379 | Redis port (when using Redis provider) |

## Core Features

### Domain Model
The system manages three primary entities with relationships:

[Diagram: Entity Relationship - Shows City, Restaurant, and Dish entities with their relationships]

- **City**: Contains basic information about cities
- **Restaurant**: Belongs to a city, contains menu of dishes
- **Dish**: Belongs to a restaurant, contains price and description

### RESTful API
The application exposes a comprehensive REST API for managing all entities:
- CRUD operations for cities, restaurants, and dishes
- Pagination support for list operations
- Input validation using Bean Validation
- Error handling with descriptive messages

### Cache Management
The system implements sophisticated caching strategies:
- Entity-level caching for cities, restaurants, and dishes
- Cache eviction on related entity changes
- Cache statistics available via Actuator endpoints

## Cache Implementation Details

### Caching Strategy
The project implements a sophisticated caching strategy that balances performance, consistency, and complexity:

[Diagram: Cache Strategy - Shows the caching decision points and eviction patterns]

1. **Cache at Controller Level**:
   - Caching is implemented at the controller layer rather than service layer
   - Rationale: This captures the fully-assembled responses including DTOs, reducing repeated transformations

2. **Multiple Cache Regions**:
   - Three separate cache regions: `CITIES`, `RESTAURANTS`, `DISHES`
   - Rationale: Allows for targeted cache evictions without clearing unrelated data

3. **Related Entity Cache Eviction**:
   - When a related entity changes, the parent entity's cache is evicted
   - Example: Adding a dish to a restaurant evicts the restaurant from cache
   - Rationale: Maintains cache coherence without complex invalidation logic

### Cache Configuration
The caching behavior is configured differently for each provider:

#### Simple Cache (Default)
```properties
spring.cache.type=simple
spring.cache.cache-names=CITIES,RESTAURANTS,DISHES
```

#### Caffeine Cache
```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=initialCapacity=100, maximumSize=1000, expireAfterAccess=1h, recordStats
```

#### Redis Cache
```properties
spring.cache.type=redis
spring.cache.redis.time-to-live=1h
spring.cache.redis.enable-statistics=true
```

### Cache Annotations
The project uses Spring's cache annotations extensively:

| Annotation | Purpose | Example Usage |
|------------|---------|--------------|
| `@Cacheable` | Store method results in cache | Used for GET operations |
| `@CachePut` | Update cache without affecting method execution | Used for POST/PUT operations |
| `@CacheEvict` | Remove entries from cache | Used when data becomes stale |
| `@Caching` | Group multiple cache operations | Used for complex cache patterns |

### Advanced Cache Patterns
The application demonstrates several advanced caching patterns:

1. **Composite Keys**:
   ```java
   @Cacheable(cacheNames = DISHES, key = "{#restaurantId,#dishId}")
   ```
   - Allows caching with multiple parameters as key
   - Used for nested resource access

2. **Conditional Caching**:
   - Cache eviction based on business conditions
   - Example: Only evict city cache when restaurant location changes

3. **Cache Statistics**:
   - Prometheus metrics for cache hit/miss rates
   - Accessible via `/actuator/prometheus` endpoint

## API Endpoints

The API provides the following endpoints for managing restaurant data:

### City Endpoints

| Method | Endpoint | Description | Cache Behavior |
|--------|----------|-------------|---------------|
| `GET` | `/api/cities/{cityId}` | Get a city by ID | Cached by cityId |
| `GET` | `/api/cities` | List all cities (paginated) | Not cached |
| `POST` | `/api/cities` | Create a new city | Result cached |
| `DELETE` | `/api/cities/{cityId}` | Delete a city | Evicts from cache |

### Restaurant Endpoints

| Method | Endpoint | Description | Cache Behavior |
|--------|----------|-------------|---------------|
| `GET` | `/api/restaurants/{restaurantId}` | Get a restaurant by ID | Cached by restaurantId |
| `GET` | `/api/restaurants` | List all restaurants (paginated) | Not cached |
| `POST` | `/api/restaurants` | Create a new restaurant | Result cached, evicts city |
| `PUT` | `/api/restaurants/{restaurantId}` | Update a restaurant | Updates cache, may evict city |
| `DELETE` | `/api/restaurants/{restaurantId}` | Delete a restaurant | Evicts from cache |

### Dish Endpoints

| Method | Endpoint | Description | Cache Behavior |
|--------|----------|-------------|---------------|
| `GET` | `/api/restaurants/{restaurantId}/dishes/{dishId}` | Get a dish | Cached by composite key |
| `GET` | `/api/restaurants/{restaurantId}/dishes` | List restaurant dishes | Cached by restaurantId |
| `POST` | `/api/restaurants/{restaurantId}/dishes` | Add a dish | Result cached, evicts restaurant |
| `PUT` | `/api/restaurants/{restaurantId}/dishes/{dishId}` | Update a dish | Updates cache, evicts restaurant |
| `DELETE` | `/api/restaurants/{restaurantId}/dishes/{dishId}` | Delete a dish | Evicts from cache |

## Testing Strategy

### Test Categories
The project implements a comprehensive testing strategy:

1. **Unit Tests**:
   - Test individual components in isolation
   - Mock dependencies using Mockito
   - Focus on business logic and caching behavior

2. **Integration Tests**:
   - Test interaction between components
   - Use real database instance via Testcontainers
   - Verify caching behavior across components

3. **Cache-Specific Tests**:
   - Verify correct caching and eviction patterns
   - Test each cache provider implementation
   - Validate cache consistency

### Test Implementation
The test suite is organized by feature area:

| Test Class | Purpose | Key Aspects Tested |
|------------|---------|-------------------|
| `CityIT` | Integration tests for City operations | City CRUD operations with caching |
| `RestaurantIT` | Integration tests for Restaurant operations | Restaurant operations with cascading cache effects |
| `RestaurantDishIT` | Integration tests for Dish operations | Nested resource operations and cache consistency |
| `CityControllerTest` | Unit tests for City controller | Cache hit/miss behavior |
| `RestaurantControllerTest` | Unit tests for Restaurant controller | Complex caching patterns |
| `RestaurantDishControllerTest` | Unit tests for Dish controller | Composite key caching |

### Testing Cache Providers
Tests can be run against different cache providers to validate consistent behavior:

```bash
# Test with Simple cache
./mvnw clean verify --projects restaurant-api

# Test with Caffeine cache
./mvnw clean verify --projects restaurant-api -DargLine="-Dspring.profiles.active=caffeine"

# Test with Redis cache
./mvnw clean verify --projects restaurant-api -DargLine="-Dspring.profiles.active=redis"
```

## Development & Contribution

### Project Structure
The project follows a standard Spring Boot structure with modular organization:

```
restaurant-api/
├── src/
│   ├── main/
│   │   ├── java/com/kirtan/restaurantapi/
│   │   │   ├── config/         # Spring configuration classes
│   │   │   ├── exception/      # Custom exceptions
│   │   │   ├── mapper/         # MapStruct DTOs mappers
│   │   │   ├── model/          # Domain entities
│   │   │   ├── repository/     # Neo4j data repositories
│   │   │   ├── rest/           # REST controllers and DTOs
│   │   │   ├── runner/         # Application runners
│   │   │   └── service/        # Business logic services
│   │   └── resources/          # Application properties
│   └── test/                   # Test classes
```

### Key Design Patterns Used

1. **Repository Pattern**:
   - Abstracts data access operations
   - Implemented via Spring Data Neo4j repositories

2. **DTO Pattern**:
   - Separates API contracts from internal models
   - Implemented using record classes for immutability

3. **Service Layer Pattern**:
   - Encapsulates business logic
   - Validation and transaction management

4. **Mapper Pattern**:
   - Clean transformation between entities and DTOs
   - Generated by MapStruct for type safety

### Build and Deployment

The project uses Maven for build management and provides scripts for Docker deployment:

- `build-docker-images.sh`: Creates Docker images (supports JVM and native builds)
- `docker-compose.yml`: Defines infrastructure services
- `remove-docker-images.sh`: Cleans up Docker images

### Contribution Guidelines

To contribute to the project:

1. Fork the repository
2. Create a feature branch
3. Implement changes with tests
4. Submit a pull request

Ensure all tests pass before submitting:
```bash
./mvnw clean verify --projects restaurant-api
```

## Future Enhancements

The project roadmap includes several planned enhancements:

1. **AOP Logging**:
   - Implement aspect-oriented logging for endpoint calls
   - Track request/response times and cache performance

2. **Data Initialization**:
   - Create sample data scripts using Neo4j API
   - Automated data seeding for testing and demos


<div align="center">
<p>Made with ❤️ by <a href="https://github.com/kirtanlab">Kirtan</a></p>
<p>MIT License</p>
</div>
