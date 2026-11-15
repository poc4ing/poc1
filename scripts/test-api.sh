#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
API_KEY="${API_KEY:-ing-mortgage-local-key}"

echo "=== 1. Health Check ==="
curl -sS "${BASE_URL}/actuator/health"
echo -e "\n"

echo "=== 2. Jolokia JMX Metadata ==="
curl -sS "${BASE_URL}/actuator/jolokia"
echo -e "\n"

echo "=== 3. Ehcache Caches ==="
curl -sS "${BASE_URL}/actuator/caches"
echo -e "\n"

echo "=== 4. Prometheus Metrics (first 15 lines) ==="
curl -sS "${BASE_URL}/actuator/prometheus" | head -n 15
echo -e "\n"

echo "=== 5. GET /api/v1/rates (default page) ==="
curl -sS "${BASE_URL}/api/v1/rates" \
  -H "X-API-Key: ${API_KEY}" \
  -H "Accept: application/json"
echo -e "\n"

echo "=== 6. GET /api/v1/rates (custom pagination & sort) ==="
curl -sS "${BASE_URL}/api/v1/rates?page=0&size=3&sort=maturityPeriod,desc" \
  -H "X-API-Key: ${API_KEY}" \
  -H "Accept: application/json"
echo -e "\n"

echo "=== 7. POST /api/v1/mortgage-check (feasible mortgage) ==="
curl -sS -X POST "${BASE_URL}/api/v1/mortgage-check" \
  -H "X-API-Key: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "income": 60000.00,
    "maturityPeriod": 20,
    "loanValue": 200000.00,
    "homeValue": 250000.00
  }'
echo -e "\n"

echo "=== 8. POST /api/v1/mortgage-check (not feasible - exceeds 4x income) ==="
curl -sS -X POST "${BASE_URL}/api/v1/mortgage-check" \
  -H "X-API-Key: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "income": 40000.00,
    "maturityPeriod": 20,
    "loanValue": 200000.00,
    "homeValue": 250000.00
  }'
echo -e "\n"

echo "=== 9. POST /api/v1/mortgage-check (invalid maturityPeriod -> 400 ProblemDetail) ==="
curl -sS -X POST "${BASE_URL}/api/v1/mortgage-check" \
  -H "X-API-Key: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -d '{
    "income": 60000.00,
    "maturityPeriod": 99,
    "loanValue": 200000.00,
    "homeValue": 250000.00
  }'
echo -e "\n"

echo "=== 10. GET /api/v1/rates without API key (-> 401 Unauthorized) ==="
curl -sS "${BASE_URL}/api/v1/rates" \
  -H "Accept: application/json"
echo -e "\n"

