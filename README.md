# kyoe-ing-mortgage

Backend service for mortgage rate lookup and feasibility calculation, built with Java 26 and Spring Boot 4.0.6 (`1.0-SNAPSHOT`).

## Modules

- `api` — OpenAPI 3.0.3 contract (`openapi.yml` and `schemas/components.yml`).
- `domain` — Core domain models (`Rate`, `MortgageCheck`, `MortgageCheckResult`) and domain exceptions.
- `application` — Use cases (`GetRatesUseCase`, `CheckMortgageUseCase`) and outbound port (`RatePersistencyPort`).
- `h2` — JPA persistence adapter backed by in-memory H2 (`RATES` table, `RATES_SEQ` sequence, UUIDv7 `idApi`) and JSR-107 Ehcache 3 caching (`ratesPageCache`, `rateByMaturityCache`).
- `persistency-mock` — In-memory map adapter for standalone mock testing.
- `rest` — Spring MVC controllers (`version = "v1"`), `X-API-Key` filter, MapStruct mappers, RFC 7807 exception handling, Actuator endpoints (`prometheus`, `jolokia`, `caches`), and Karate tests.
- `service` — Spring Boot executable application.

## API

Base path: `/api/v1`

All `/api/**` endpoints require the `X-API-Key` header (default in `local`: `ing-mortgage-local-key`, configurable via `MORTGAGE_API_KEY`).

Ready-to-run smoke test scripts with `curl` commands against a deployed instance (`BASE_URL` defaults to `http://localhost:8080`):
- Bash: `./scripts/test-api.sh`
- PowerShell: `.\scripts\test-api.ps1`

### `GET /api/v1/rates`
Returns a paginated list of mortgage interest rates (`PageRateDto`). Supports standard Spring Data pagination query parameters (`page`, `size`, `sort`).

```bash
curl "http://localhost:8080/api/v1/rates?page=0&size=10&sort=maturityPeriod,asc" \
  -H "X-API-Key: ing-mortgage-local-key"
```

### `POST /api/v1/mortgage-check`
Checks whether a loan is feasible (`loanValue <= 4 * income` and `loanValue <= homeValue`) and calculates the monthly payment for the given maturity period.

```bash
curl -X POST "http://localhost:8080/api/v1/mortgage-check" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: ing-mortgage-local-key" \
  -d '{
    "income": 60000.00,
    "maturityPeriod": 20,
    "loanValue": 200000.00,
    "homeValue": 250000.00
  }'
```

## Actuator & Monitoring

Exposed Actuator endpoints:
- `/actuator/health` — Readiness and liveness details
- `/actuator/prometheus` — Prometheus metrics scrape target
- `/actuator/jolokia` — Jolokia JMX metadata and runtime MBean summary
- `/actuator/caches` — Registered Ehcache 3 caches (`ratesPageCache`, `rateByMaturityCache`)

To start the local Prometheus (`:9090`), Alertmanager (`:9093`), and Grafana (`:3000`, `admin`/`admin`) stack:

```bash
docker compose up -d
```

## Profiles

Available Maven and Spring profiles: `local` (default), `dev`, `int`, `pro`.

## Build & Run

Requirements: JDK 26+, Maven 3.9+.

```bash
mvn clean install
```

Run locally:

```bash
./run-server.sh
```

Or directly:

```bash
java -Dspring.profiles.active=local -jar service/target/kyoe-ing-mortgage-service-1.0-SNAPSHOT.jar
```
