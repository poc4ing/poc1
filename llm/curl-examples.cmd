@echo off
REM Ejemplos de cURL para consultas en lenguaje natural al módulo LLM (Spring AI + MCP)

echo 1. Listar switches mediante consulta en lenguaje natural:
curl -X POST http://localhost:8082/api/v1/llm/chat ^
  -H "Content-Type: application/json" ^
  -d "{\"prompt\": \"Mostrar todos los feature switches activos\"}"

echo.
echo 2. Crear un nuevo switch en lenguaje natural:
curl -X POST http://localhost:8082/api/v1/llm/chat ^
  -H "Content-Type: application/json" ^
  -d "{\"prompt\": \"Crear un nuevo switch con nombre 'PASARELA_PAGO'\"}"

echo.
echo 3. Buscar switches por criterio:
curl -X POST http://localhost:8082/api/v1/llm/chat ^
  -H "Content-Type: application/json" ^
  -d "{\"prompt\": \"Buscar switches por criterio pago\"}"

echo.
echo 4. Obtener información del Agente IA y herramientas MCP cargadas:
curl -X GET http://localhost:8082/api/v1/llm/agent/info
