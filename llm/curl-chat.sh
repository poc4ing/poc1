#!/bin/bash
# Script de ejemplo cURL para realizar consultas en lenguaje natural al Agente IA (Spring AI) del módulo LLM

ENDPOINT="${LLM_HOST:-http://localhost:8082}/api/v1/llm/chat"

PROMPT="${1:-Mostrar todos los feature switches activos}"

echo "================================================================="
echo "Enviando consulta en lenguaje natural al módulo LLM:"
echo "Prompt: $PROMPT"
echo "Endpoint: $ENDPOINT"
echo "================================================================="

curl -s -X POST "$ENDPOINT" \
  -H "Content-Type: application/json" \
  -d "{\"prompt\": \"$PROMPT\"}" | jq . || curl -s -X POST "$ENDPOINT" -H "Content-Type: application/json" -d "{\"prompt\": \"$PROMPT\"}"

echo -e "\n================================================================="
