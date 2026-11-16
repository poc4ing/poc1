package es.mango.pulse.switches.web.infrastructure.llm.model

data class LlmChatRequestDTO(
    val prompt: String,
    val conversationId: String? = null
)

data class LlmChatResponseDTO(
    val response: String,
    val toolExecutions: List<String> = emptyList(),
    val success: Boolean = true,
    val error: String? = null
)

data class AgentInfoDTO(
    val agentName: String,
    val description: String,
    val springAiVersion: String,
    val availableMcpTools: List<String>
)
