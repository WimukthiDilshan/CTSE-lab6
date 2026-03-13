# Kafka Microservices Lab 06 - Event-Driven Architecture

A complete event-driven microservices system using Spring Boot, Apache Kafka (KRaft mode), and Spring Cloud Gateway.

## Architecture

```
Client (Postman)
      ↓
API Gateway (Port 8080)
      ↓
Order Service (8081) → Publishes Event (OrderCreated)
      ↓
   Kafka (9092) - KRaft Mode
      ↓
Inventory Service (8082) ← Consumes Event
Billing Service (8083) ← Consumes Event
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| **API Gateway** | 8080 | Routes requests to microservices |
| **Order Service** | 8081 | Creates orders & publishes Kafka events |
| **Inventory Service** | 8082 | Consumes events & updates stock |
| **Billing Service** | 8083 | Consumes events & generates invoices |
| **Kafka** | 9092 | Event broker (KRaft mode - No ZooKeeper) |

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Postman (for testing)

### Option 1: Docker Compose (Recommended)

1. **Build all services:**
   ```bash
   # Build Order Service
   cd order-service
   mvn clean package -DskipTests
   cd ..

   # Build Inventory Service
   cd inventory-service
   mvn clean package -DskipTests
   cd ..

   # Build Billing Service
   cd billing-service
   mvn clean package -DskipTests
   cd ..

   # Build API Gateway
   cd api-gateway
   mvn clean package -DskipTests
   cd ..
   ```

2. **Start all services:**
   ```bash
   docker-compose up --build
   ```

3. **Check all services are running:**
   ```bash
   docker ps
   ```

### Option 2: Run Locally

1. **Start Kafka:**
   ```bash
   docker-compose up kafka -d
   ```

2. **Start services individually:**
   ```bash
   # Terminal 1 - Order Service
   cd order-service
   mvn spring-boot:run

   # Terminal 2 - Inventory Service
   cd inventory-service
   mvn spring-boot:run

   # Terminal 3 - Billing Service
   cd billing-service
   mvn spring-boot:run

   # Terminal 4 - API Gateway
   cd api-gateway
   mvn spring-boot:run
   ```

## Testing

### Import Postman Collection
1. Import `Kafka-Microservices-Lab.postman_collection.json` into Postman
2. Collection includes all test requests

### Manual Testing

#### 1. Create Order (via API Gateway)
```bash
POST http://localhost:8080/orders
Content-Type: application/json

{
  "orderId": "ORD-1001",
  "item": "Laptop", 
  "quantity": 1,
  "unitPrice": 999.99
}
```

Expected Response:
```
Order Created & Event Published
```

#### 2. Check Service Logs
After creating an order, you should see logs in all services:

**Order Service Log:**
```
Order created with id: 1
Published OrderCreated event to Kafka: {...}
```

**Inventory Service Log:**
```
Received order event: {...}
Processing inventory update for Order ID: ORD-1001, Item: Laptop, Quantity: 1
Inventory successfully updated for order: ORD-1001
```

**Billing Service Log:**
```
Received order event for billing: {...}
Processing invoice generation for Order ID: ORD-1001, Item: Laptop, Quantity: 1, Total: 999.99
Invoice INV-XXXX generated successfully for order: ORD-1001
```

#### 3. Verify Data
```bash
# Check inventory updates
GET http://localhost:8080/inventory

# Check generated invoices
GET http://localhost:8080/billing/invoices

# Check orders
GET http://localhost:8080/orders
```

## Expected Flow

1. **Client** sends POST request to API Gateway
2. **API Gateway** routes request to Order Service
3. **Order Service** saves order to database
4. **Order Service** publishes `OrderCreated` event to Kafka topic `order-topic`
5. **Inventory Service** consumes event and updates stock
6. **Billing Service** consumes event and generates invoice
7. All services log successful processing

## 📊 Health Checks

Check if all services are healthy:

```bash
# API Gateway
GET http://localhost:8080/actuator/health

# Order Service  
GET http://localhost:8081/actuator/health

# Inventory Service
GET http://localhost:8082/actuator/health

# Billing Service
GET http://localhost:8083/actuator/health
```

## 🐳 Docker Commands

```bash
# Start all services
docker-compose up --build

# Start in background
docker-compose up -d --build

# View logs
docker-compose logs -f [service-name]

# Stop all services
docker-compose down

# Clean up
docker-compose down --volumes --rmi all
```