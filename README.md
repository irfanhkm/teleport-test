# Tracking Number Generation Services

A service that generates unique tracking numbers for shipments, with validation for Southeast Asian countries.

## Features

- Generates unique tracking numbers using Snowflake algorithm
- Validates origin and destination country codes (Southeast Asia only)
- Redis integration for tracking number uniqueness
- Graceful fallback when Redis is unavailable

## Supported Countries

The API currently supports the following Southeast Asian countries:

| Country Code | Country Name |
|-------------|--------------|
| BN          | Brunei       |
| KH          | Cambodia     |
| ID          | Indonesia    |
| LA          | Laos         |
| MY          | Malaysia     |
| MM          | Myanmar      |
| PH          | Philippines  |
| SG          | Singapore    |
| TH          | Thailand     |
| TL          | Timor-Leste  |
| VN          | Vietnam      |

## Prerequisites

- Java 17 or higher (recommend install using sdkman)
- Gradle 8.5
- Redis (optional, for tracking number uniqueness)

## Getting Started

1. Clone the repository
2. Build the project:
   ```bash
   ./gradlew build
   ```
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Generate Tracking Number

```
GET https://teleport-test.onrender.com/tracking-number
```

Query Parameters:
| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| originCountryId | String | Yes | Origin country code (2 letters) | SG |
| destinationCountryId | String | Yes | Destination country code (2 letters) | ID |
| weight | Double | Yes | Package weight in kg | 1.5 |
| createdAt | String | Yes | Creation timestamp in RFC 3339 format | 2024-04-24T12:00:00Z |
| customerId | String | Yes | Customer UUID | 123e4567-e89b-12d3-a456-426614174000 |
| customerName | String | Yes | Customer name | John Doe |
| customerSlug | String | Yes | Customer slug | john-doe |

Example Request:
```
GET https://teleport-test.onrender.com/tracking-number?originCountryId=ID&destinationCountryId=ID&weight=1.5000&createdAt=2024-04-24T12:00:00Z&customerId=123e4567-e89b-12d3-a456-426614174000&customerName=Test%20Customer&customerSlug=test-customer
```

Example Response:
```json
{
    "tracking_number": "TL5CEIBPMK1C74",
    "generated_at": "2025-04-25T04:02:49.884Z"
}
```

## Validation Rules

- Origin and destination country codes must be valid Southeast Asian country codes
- Weight must be between 0.001 and 999.999 kg
- Created at timestamp must be in RFC 3339 format
- Customer ID must be a valid UUID
- Customer name and slug must be between 1 and 100 characters

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- 400 Bad Request: Invalid input data
- 500 Internal Server Error: Server-side errors

## Configuration

The application can be configured through `application.properties`:

```properties
# Redis Configuration (optional)
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

## Testing

Run the tests with:
```bash
./gradlew test
```