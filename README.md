# Parking Management System

A Java Spring Boot application for managing parking spots and sessions with PostgreSQL database.

## Features

- **Parking Spot Management**: Create, update, delete, and query parking spots
- **Parking Session Management**: Start and end parking sessions
- **Multiple Spot Types**: Regular, Disabled, Electric Charging, Motorcycle, Truck
- **Real-time Statistics**: Available spots, occupied spots, active sessions
- **Payment Status Tracking**: Track payment status for parking sessions
- **RESTful API**: Comprehensive REST endpoints for all operations

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Database migration)
- **Lombok**
- **Gradle**

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Gradle 7.0 or higher

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE parking_db;
```

2. Create a user (optional):
```sql
CREATE USER parking_user WITH PASSWORD 'parking_password';
GRANT ALL PRIVILEGES ON DATABASE parking_db TO parking_user;
```

## Configuration

Update the database connection settings in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/parking_db
    username: parking_user
    password: parking_password
```

## Running the Application

1. **Build the project**:
```bash
./gradlew build
```

2. **Run the application**:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Parking Spots

- `GET /api/parking/spots` - Get all parking spots
- `GET /api/parking/spots/{id}` - Get parking spot by ID
- `GET /api/parking/spots/number/{spotNumber}` - Get parking spot by number
- `GET /api/parking/spots/available` - Get available parking spots
- `GET /api/parking/spots/available/{spotType}` - Get available spots by type
- `POST /api/parking/spots` - Create new parking spot
- `PUT /api/parking/spots/{id}` - Update parking spot
- `DELETE /api/parking/spots/{id}` - Delete parking spot

### Parking Sessions

- `POST /api/parking/sessions/start` - Start parking session
- `POST /api/parking/sessions/{sessionId}/end` - End parking session
- `POST /api/parking/sessions/end-by-license` - End session by license plate
- `PUT /api/parking/sessions/{sessionId}/payment` - Update payment status
- `GET /api/parking/sessions/license/{licensePlate}` - Get sessions by license plate
- `GET /api/parking/sessions/active` - Get active sessions

### Statistics

- `GET /api/parking/stats` - Get all statistics
- `GET /api/parking/stats/available-spots` - Get available spots count
- `GET /api/parking/stats/occupied-spots` - Get occupied spots count
- `GET /api/parking/stats/active-sessions` - Get active sessions count

## Example API Usage

### Start a Parking Session
```bash
curl -X POST http://localhost:8080/api/parking/sessions/start \
  -H "Content-Type: application/json" \
  -d '{
    "spotId": 1,
    "licensePlate": "ABC-123"
  }'
```

### End a Parking Session
```bash
curl -X POST http://localhost:8080/api/parking/sessions/1/end
```

### Get Available Spots
```bash
curl http://localhost:8080/api/parking/spots/available
```

### Get Statistics
```bash
curl http://localhost:8080/api/parking/stats
```

## Database Schema

### Parking Spots Table
- `id` - Primary key
- `spot_number` - Unique spot identifier
- `spot_type` - Type of parking spot (REGULAR, DISABLED, etc.)
- `status` - Current status (AVAILABLE, OCCUPIED, etc.)
- `floor_level` - Floor level of the spot
- `hourly_rate` - Hourly parking rate
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

### Parking Sessions Table
- `id` - Primary key
- `parking_spot_id` - Foreign key to parking_spots
- `license_plate` - Vehicle license plate
- `entry_time` - Session start time
- `exit_time` - Session end time
- `total_amount` - Total parking fee
- `status` - Session status (ACTIVE, COMPLETED, etc.)
- `payment_status` - Payment status (PENDING, PAID, etc.)
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

## Spot Types

- `REGULAR` - Standard parking spots
- `DISABLED` - Accessible parking spots
- `ELECTRIC_CHARGING` - Spots with electric charging
- `MOTORCYCLE` - Motorcycle parking spots
- `TRUCK` - Large vehicle parking spots

## Development

### Running Tests
```bash
./gradlew test
```

### Database Migration
The application uses Flyway for database migrations. Migration scripts are located in `src/main/resources/db/migration/`.

### Adding New Features
1. Create entity classes in `src/main/java/com/parking/entity/`
2. Create repository interfaces in `src/main/java/com/parking/repository/`
3. Add business logic in `src/main/java/com/parking/service/`
4. Create REST endpoints in `src/main/java/com/parking/controller/`
5. Add database migrations if needed

## License

This project is licensed under the MIT License. "# parking" 
