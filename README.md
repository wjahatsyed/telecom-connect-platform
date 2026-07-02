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

## Health Checks

Each service exposes:

- `GET /health`
- `GET /actuator/health`

Service ports are assigned in each module's `application.yml`.

## Next Milestones

- Add OpenAPI contracts per service
- Introduce PostgreSQL schemas and Flyway migrations
- Add Kafka topic design and event contracts
- Add Redis-backed caching where it earns its keep
- Add Kubernetes manifests
- Add GitHub Actions CI
- Add Micrometer/OpenTelemetry and Resilience4j
