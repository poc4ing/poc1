package es.mango.pulse.switches.web.infrastructure.mcp

import es.kyoe.ing.mortgage.web.infrastructure.rest.api.MortgageCheckApi
import es.kyoe.ing.mortgage.web.infrastructure.rest.api.RatesApi
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpRequestDTO
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpResponseDTO
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpToolDTO
import es.mango.vulcan.controlcenter.web.infrastructure.rest.api.WorkstationApi
import es.mango.vulcan.controlcenter.web.infrastructure.rest.model.PageableWorkstation
import es.mango.vulcan.controlcenter.web.infrastructure.rest.model.Workstation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
open class MortgageMcpAdapter @Autowired constructor(
    private val ratesApi: RatesApi,
    private val mortgageChekApi: MortgageCheckApi
) : WorkstationsMcpInterface {

    override fun listTools(): List<McpToolDTO> = listOf(
        McpToolDTO(
            name = "getRates",
            description = "Retrieve list of rates",
            inputSchema = mapOf(
                "type" to "object",
                "properties" to mapOf(
                    "page" to mapOf("type" to "integer", "description" to "Número de página (0-indexed)"),
                    "size" to mapOf("type" to "integer", "description" to "Tamaño de página"),
                    "sort" to mapOf("type" to "string", "description" to "Campo para ordenar")
                )
            )
        ),
        McpToolDTO(
            name = "mortgageCheck",
            description = "Check mortgage feasibility.",
            inputSchema = mapOf(
                "type" to "object",
                "properties" to mapOf(
                    "id" to mapOf("type" to "string", "format" to "uuid")
                ),
                "required" to listOf("id")
            )
        )
    )

    override fun executeTool(request: McpRequestDTO): McpResponseDTO {
        return try {
            val args = request.arguments
            val result: Any? = when (request.tool) {
                "getRates" -> {
                    val page = (args["page"] as? Number)?.toInt()
                    val size = (args["size"] as? Number)?.toInt()
                    val sort = args["sort"] as? String
                    getRates(page, size, sort).body
                }

                "restartWorkstaion" -> {
                    val idStr = args["id"] as? String ?: throw IllegalArgumentException("Se requiere el parámetro 'id'")
                    val xApiKey = args["xApiKey"] as String
                    restartWorkstation(xApiKey,UUID.fromString(idStr)).body
                }
                else -> throw IllegalArgumentException("Herramienta MCP desconocida: ${request.tool}")
            }

            McpResponseDTO(
                status = "SUCCESS",
                tool = request.tool,
                result = result
            )
        } catch (e: Exception) {
            McpResponseDTO(
                status = "ERROR",
                tool = request.tool,
                error = e.message ?: "Error desconocido durante la ejecución de la herramienta MCP"
            )
        }
    }

    override fun getRates(page: Int?, size: Int?, sort: String?): ResponseEntity<PageableWorkstation> {
        return ratesApi.getRates(page, size, sort)
    }

    override fun checkMortgage(xApiKey: String,id: UUID): ResponseEntity<Workstation> {
        return mortgageChekApi.restartWorkstation(xApiKey,id)
    }

}
