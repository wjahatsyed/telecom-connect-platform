# Telecom Connect Platform

Telecom Connect Platform is a production-style Java backend portfolio project for a global telecom and eSIM connectivity platform. It models the core backend capabilities behind customer onboarding, device registration, eSIM provisioning, subscriptions, usage ingestion, billing, notifications, and API edge access.

The goal is to demonstrate senior backend engineering practices with a realistic multi-service architecture, clean module boundaries, operational awareness, and incremental delivery.

## Current Foundation

- Java 21
- Spring Boot 3
- Maven multi-module monorepo
- PostgreSQL, Redis, Kafka, and Zookeeper via Docker Compose
- Minimal Spring Boot application per service
- Service health endpoints

## Services

| Service | Responsibility |
| --- | --- |
| `identity-service` | Authentication, authorization, tenants, and service identities |
| `customer-service` | Customer accounts, profiles, and organization data |
| `device-service` | Registered devices, IMEIs, and device lifecycle state |
| `esim-provisioning-service` | eSIM activation, profile download orchestration, and provider integration |
| `subscription-service` | Plans, subscriptions, bundles, and entitlement state |
| `usage-service` | Usage event ingestion, aggregation, and rating inputs |
| `billing-service` | Invoices, charges, payments, and billing workflows |
| `notification-service` | Email, SMS, webhook, and operational notifications |
| `api-gateway` | External API entry point and cross-cutting edge concerns |

## Repository Layout

```text
docs/
  architecture/
  adr/
  api/
  runbooks/
platform/
  docker/
  k8s/
  observability/
scripts/
services/
  identity-service/
  customer-service/
  device-service/
  esim-provisioning-service/
  subscription-service/
  usage-service/
  billing-service/
  notification-service/
  api-gateway/
```

## Build

```bash
mvn clean install
```

## Local Infrastructure

```bash
docker compose -f platform/docker/docker-compose.yml up -d
```

Stop the local infrastructure:

```bash
docker compose -f platform/docker/docker-compose.yml down
```

PostgreSQL is exposed locally on `localhost:5432`:

- Database: `telecom_connect`
- Username: `telecom`
- Password: `telecom`

The core services use Spring Data JPA with Flyway migrations. Each service owns its tables and stores foreign identifiers from other services as plain UUID values. There are intentionally no cross-service foreign keys or direct joins.

Because the local Docker Compose setup uses one PostgreSQL database for convenience, each service uses its own Flyway history table:

- `customer-service`: `flyway_schema_history_customer`
- `device-service`: `flyway_schema_history_device`
- `subscription-service`: `flyway_schema_history_subscription`
- `esim-provisioning-service`: `flyway_schema_history_esim`

Run a service against the local database:

```bash
mvn -pl services/customer-service spring-boot:run
```

Run tests, including PostgreSQL Testcontainers integration tests:

```bash
mvn test
```

Run one focused service test suite:

```bash
mvn -pl services/esim-provisioning-service test
```

## Health Checks

Each service exposes:

- `GET /health`
- `GET /actuator/health`

Service ports are assigned in each module's `application.yml`.

## API Overview

The first domain slice uses in-memory persistence so each service can run independently while the API and domain boundaries settle.

| Service | Port | APIs |
| --- | --- | --- |
| `customer-service` | `8082` | `POST /customers`, `GET /customers/{customerId}`, `GET /customers` |
| `device-service` | `8083` | `POST /devices`, `GET /devices/{deviceId}`, `GET /devices/customer/{customerId}` |
| `subscription-service` | `8085` | `POST /subscriptions`, `GET /subscriptions/{subscriptionId}`, `GET /subscriptions/device/{deviceId}`, lifecycle actions |
| `esim-provisioning-service` | `8084` | `POST /esims/provision`, `GET /esims/{iccid}`, lifecycle actions |

Subscription lifecycle actions:

- `POST /subscriptions/{subscriptionId}/activate`
- `POST /subscriptions/{subscriptionId}/suspend`
- `POST /subscriptions/{subscriptionId}/resume`
- `POST /subscriptions/{subscriptionId}/cancel`

eSIM lifecycle actions:

- `POST /esims/{iccid}/activate`
- `POST /esims/{iccid}/suspend`
- `POST /esims/{iccid}/terminate`

## Example Requests

Create a customer:

```bash
curl -X POST http://localhost:8082/customers \
  -H "Content-Type: application/json" \
  -d '{
    "type": "ENTERPRISE",
    "displayName": "Acme Connectivity",
    "email": "ops@acme.example",
    "phoneNumber": "+15551234567"
  }'
```

Create a device:

```bash
curl -X POST http://localhost:8083/devices \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "00000000-0000-0000-0000-000000000001",
    "type": "IOT_SENSOR",
    "name": "Cold Chain Sensor",
    "imei": "123456789012345"
  }'
```

Create and activate a subscription:

```bash
curl -X POST http://localhost:8085/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "00000000-0000-0000-0000-000000000001",
    "deviceId": "00000000-0000-0000-0000-000000000002",
    "planCode": "GLOBAL_5GB"
  }'

curl -X POST http://localhost:8085/subscriptions/{subscriptionId}/activate
```

Provision and activate an eSIM:

```bash
curl -X POST http://localhost:8084/esims/provision \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "00000000-0000-0000-0000-000000000001",
    "deviceId": "00000000-0000-0000-0000-000000000002",
    "subscriptionId": "00000000-0000-0000-0000-000000000003"
  }'

curl -X POST http://localhost:8084/esims/{iccid}/activate
```

## Next Milestones

- Add OpenAPI contracts per service
- Introduce PostgreSQL schemas and Flyway migrations
- Add Kafka topic design and event contracts
- Add Redis-backed caching where it earns its keep
- Add Kubernetes manifests
- Add GitHub Actions CI
- Add Micrometer/OpenTelemetry and Resilience4j
