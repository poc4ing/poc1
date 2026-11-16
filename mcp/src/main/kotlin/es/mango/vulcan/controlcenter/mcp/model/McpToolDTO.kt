package es.mango.pulse.switches.web.infrastructure.mcp.model

data class McpToolDTO(
    val name: String,
    val description: String,
    val inputSchema: Map<String, Any?> = emptyMap()
)

data class McpRequestDTO(
    val tool: String,
    val arguments: Map<String, Any?> = emptyMap()
)

data class McpResponseDTO(
    val status: String,
    val tool: String,
    val result: Any? = null,
    val error: String? = null
)
