package es.mango.pulse.switches.web.infrastructure.llm

import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.store.embedding.CosineSimilarity
import es.mango.pulse.switches.web.infrastructure.llm.model.AgentInfoDTO
import es.mango.pulse.switches.web.infrastructure.llm.model.LlmChatRequestDTO
import es.mango.pulse.switches.web.infrastructure.llm.model.LlmChatResponseDTO
import es.mango.pulse.switches.web.infrastructure.mcp.WorkstationsMcpInterface
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpRequestDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Locale
import java.util.UUID

@Service
open class WorkstationsLlmAgentService @Autowired constructor(
    private val mcpInterface: WorkstationsMcpInterface,
    private val embeddingModel: EmbeddingModel
) {

    companion object {
        private const val SIMILARITY_THRESHOLD = 0.48
    }

    private val log = LoggerFactory.getLogger(javaClass)

    enum class ToolIntent(
        val toolName: String,
        val exemplarPhrases: List<String>
    ) {
        GET_WORKSTATIONS(
            "getWorkstations",
            listOf(
                "listar todos los workstations",
                "dame todos los workstations disponibles",
                "ver workstations",
                "mostrar todos los feature workstations",
                "list all workstations",
                "obtener todos los workstations"
            )
        ),
        RESTART_WORKSTATION(
            "restartWorkstation",
            listOf(
                "reinicializar workstation"
            )
        )
    }

    private val intentEmbeddings: Map<ToolIntent, List<Embedding>> by lazy {
        log.info("Inicializando embeddings semánticos para intenciones del agente LLM...")
        ToolIntent.entries.associateWith { intent ->
            intent.exemplarPhrases.map { phrase ->
                embeddingModel.embed(phrase).content()
            }
        }
    }

    fun processPrompt(request: LlmChatRequestDTO): LlmChatResponseDTO {
        val prompt = request.prompt.trim()
        val executedTools = mutableListOf<String>()

        return try {
            val promptEmbedding = embeddingModel.embed(prompt).content()

            val intentScores = ToolIntent.entries.map { intent ->
                val exemplars = intentEmbeddings[intent] ?: emptyList()
                val maxScore = exemplars.maxOfOrNull { exemplar ->
                    CosineSimilarity.between(promptEmbedding, exemplar)
                } ?: 0.0
                Pair(intent, maxScore)
            }

            val bestMatch = intentScores.maxByOrNull { it.second }
            val bestIntent = bestMatch?.first
            val similarityScore = bestMatch?.second ?: 0.0

            log.info(
                "Prompt: '{}' -> Intent detectado: {} con similitud semántica: {}",
                prompt,
                bestIntent,
                formatScore(similarityScore)
            )

            val (responseMessage, _) = if (similarityScore < SIMILARITY_THRESHOLD || bestIntent == null) {
                val toolsList = mcpInterface.listTools().map { it.name }
                Pair(
                    "Respuesta del Agente IA (LangChain4j all-MiniLM-L6-v2): Hola, soy el agente de Feature Workstations impulsado por LangChain4j. " +
                        "Utilizo un modelo neural de embeddings en memoria (all-MiniLM-L6-v2) para interpretar semánticamente tus peticiones en lenguaje natural. " +
                        "Herramientas MCP disponibles: $toolsList",
                    "none"
                )
            } else {
                when (bestIntent) {
                    ToolIntent.GET_WORKSTATIONS -> {
                        val tool = ToolIntent.GET_WORKSTATIONS.toolName
                        executedTools.add(tool)
                        val mcpResponse = mcpInterface.executeTool(McpRequestDTO(tool, mapOf("page" to 0, "size" to 50)))
                        val content = mcpResponse.result ?: "No se encontraron switches."
                        Pair(
                            "Respuesta del Agente IA (LangChain4j all-MiniLM-L6-v2, similitud semántica: ${formatScore(similarityScore)}):\n" +
                                "Se obtuvieron los workstations correctamente mediante la herramienta MCP $tool:\n$content",
                            tool
                        )
                    }

                    ToolIntent.RESTART_WORKSTATION -> {
                        val tool = ToolIntent.RESTART_WORKSTATION.toolName
                        val uuidMatch = extractUuid(prompt)
                        if (uuidMatch != null) {
                            executedTools.add(tool)
                            val mcpResponse = mcpInterface.executeTool(McpRequestDTO(tool, mapOf("id" to uuidMatch.toString())))
                            val content = mcpResponse.result ?: "Workstation reinicializado."
                            Pair(
                                "Respuesta del Agente IA (LangChain4j all-MiniLM-L6-v2, similitud semántica: ${formatScore(similarityScore)}):\n" +
                                    "Se reinicializoel workstation $uuidMatch mediante la herramienta MCP $tool:\n$content",
                                tool
                            )
                        } else {
                            Pair(
                                "Respuesta del Agente IA (LangChain4j all-MiniLM-L6-v2, similitud: ${formatScore(similarityScore)}): " +
                                    "He detectado la intención de alternar el estado de un switch, pero por favor proporciona un UUID válido.",
                                tool
                            )
                        }
                    }

                }
            }

            LlmChatResponseDTO(
                response = responseMessage,
                toolExecutions = executedTools,
                success = true
            )
        } catch (e: Exception) {
            log.error("Error al procesar prompt con el agente semántico LangChain4j", e)
            LlmChatResponseDTO(
                response = "Error al procesar la petición con el Agente IA: ${e.message}",
                toolExecutions = executedTools,
                success = false,
                error = e.message
            )
        }
    }

    fun getAgentInfo(): AgentInfoDTO {
        val tools = mcpInterface.listTools().map { "${it.name}: ${it.description}" }
        return AgentInfoDTO(
            agentName = "LangChain4j-Pulse-Switches-Agent (all-MiniLM-L6-v2)",
            description = "Agente de Inteligencia Artificial que procesa lenguaje natural mediante el modelo neural ONNX dev.langchain4j:langchain4j-embeddings-all-minilm-l6-v2 embebido en memoria de la JVM y clasifica semánticamente las intenciones hacia herramientas MCP.",
            springAiVersion = "dev.langchain4j:0.33.0",
            availableMcpTools = tools
        )
    }

    private fun formatScore(score: Double): String {
        return String.format(Locale.US, "%.4f", score)
    }

    private fun extractUuid(text: String): UUID? {
        val regex = Regex("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
        val match = regex.find(text)
        return match?.value?.let { UUID.fromString(it) }
    }

    private fun extractCriteria(text: String): String {
        val words = text.split(" ")
        return words.lastOrNull { it.isNotBlank() && !it.equals("buscar", ignoreCase = true) && !it.equals("filtrar", ignoreCase = true) } ?: "default"
    }

    private fun extractNameForCreation(text: String): String {
        val regex = Regex("'(.*?)'|\"(.*?)\"")
        val match = regex.find(text)
        return match?.groupValues?.get(1)?.ifBlank { null }
            ?: match?.groupValues?.get(2)?.ifBlank { null }
            ?: "NEW_FEATURE_SWITCH_${System.currentTimeMillis()}"
    }
}
