# Módulo LLM - Agente LangChain4j con Embeddings all-MiniLM-L6-v2 y MCP

Este módulo implementa un **Agente de Inteligencia Artificial (LangChain4j)** que procesa consultas en lenguaje natural mediante el modelo neural ONNX embebido en memoria de la JVM (`dev.langchain4j:langchain4j-embeddings-all-minilm-l6-v2`) clasificando semánticamente las intenciones del usuario por similitud coseno y ejecutando las acciones correspondientes a través del módulo **MCP** (`pulse-switches-mcp`).

---

## 🚀 Ejemplos de Peticiones cURL (Lenguaje Natural)

### 1. Listar Feature Switches
```bash
curl -X POST http://localhost:8082/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Mostrar todos los feature switches activos"}'
```

### 2. Crear un Nuevo Feature Switch
```bash
curl -X POST http://localhost:8082/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Crear un nuevo switch con nombre '\''NUEVO_CHECKOUT_V2'\''"}'
```

### 3. Filtrar / Buscar por Criterio
```bash
curl -X POST http://localhost:8082/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Buscar switches por criterio promo"}'
```

### 4. Alternar Estado (Toggle) de un Switch por UUID
```bash
curl -X POST http://localhost:8082/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Alternar el estado del switch 12345678-1234-1234-1234-123456789abc"}'
```

### 5. Eliminar un Switch por UUID
```bash
curl -X POST http://localhost:8082/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Eliminar el switch con id 12345678-1234-1234-1234-123456789abc"}'
```

### 6. Consultar Información del Agente IA y Herramientas MCP
```bash
curl -X GET http://localhost:8082/api/v1/llm/agent/info
```

---

## 📜 Archivos de Ejecución Rápida
- **Bash (Linux / macOS / Git Bash)**: `./curl-chat.sh "Tu consulta en lenguaje natural"`
- **CMD (Windows)**: `curl-examples.cmd`
