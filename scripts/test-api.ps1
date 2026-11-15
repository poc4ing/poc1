$ErrorActionPreference = "Stop"

$BaseUrl = if ($env:BASE_URL) { $env:BASE_URL } else { "http://localhost:8080" }
$ApiKey  = if ($env:API_KEY)  { $env:API_KEY }  else { "ing-mortgage-local-key" }

Write-Host "=== 1. Health Check ==="
curl.exe -sS "$BaseUrl/actuator/health"
Write-Host "`n"

Write-Host "=== 2. Jolokia JMX Metadata ==="
curl.exe -sS "$BaseUrl/actuator/jolokia"
Write-Host "`n"

Write-Host "=== 3. Ehcache Caches ==="
curl.exe -sS "$BaseUrl/actuator/caches"
Write-Host "`n"

Write-Host "=== 4. GET /api/v1/rates (default page) ==="
curl.exe -sS "$BaseUrl/api/v1/rates" `
  -H "X-API-Key: $ApiKey" `
  -H "Accept: application/json"
Write-Host "`n"

Write-Host "=== 5. GET /api/v1/rates (custom pagination & sort) ==="
curl.exe -sS "$BaseUrl/api/v1/rates?page=0&size=3&sort=maturityPeriod,desc" `
  -H "X-API-Key: $ApiKey" `
  -H "Accept: application/json"
Write-Host "`n"

Write-Host "=== 6. POST /api/v1/mortgage-check (feasible mortgage) ==="
curl.exe -sS -X POST "$BaseUrl/api/v1/mortgage-check" `
  -H "X-API-Key: $ApiKey" `
  -H "Content-Type: application/json" `
  -d '{\"income\":60000.00,\"maturityPeriod\":20,\"loanValue\":200000.00,\"homeValue\":250000.00}'
Write-Host "`n"

Write-Host "=== 7. POST /api/v1/mortgage-check (not feasible - exceeds 4x income) ==="
curl.exe -sS -X POST "$BaseUrl/api/v1/mortgage-check" `
  -H "X-API-Key: $ApiKey" `
  -H "Content-Type: application/json" `
  -d '{\"income\":40000.00,\"maturityPeriod\":20,\"loanValue\":200000.00,\"homeValue\":250000.00}'
Write-Host "`n"

Write-Host "=== 8. POST /api/v1/mortgage-check (invalid maturityPeriod -> 400 ProblemDetail) ==="
curl.exe -sS -X POST "$BaseUrl/api/v1/mortgage-check" `
  -H "X-API-Key: $ApiKey" `
  -H "Content-Type: application/json" `
  -d '{\"income\":60000.00,\"maturityPeriod\":99,\"loanValue\":200000.00,\"homeValue\":250000.00}'
Write-Host "`n"

Write-Host "=== 9. GET /api/v1/rates without API key (-> 401 Unauthorized) ==="
curl.exe -sS "$BaseUrl/api/v1/rates" `
  -H "Accept: application/json"
Write-Host "`n"

