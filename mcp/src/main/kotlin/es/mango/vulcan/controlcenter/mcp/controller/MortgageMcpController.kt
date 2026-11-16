package es.mango.pulse.switches.web.infrastructure.mcp.controller

import es.mango.pulse.switches.web.infrastructure.mcp.WorkstationsMcpInterface
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpRequestDTO
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpResponseDTO
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpToolDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/mcp")
open class MortgageMcpController @Autowired constructor(
    private val mcpInterface: WorkstationsMcpInterface
) {

    @GetMapping("/tools")
    fun listTools(): ResponseEntity<List<McpToolDTO>> {
        return ResponseEntity.ok(mcpInterface.listTools())
    }

    @PostMapping("/execute")
    fun executeTool(@RequestBody request: McpRequestDTO): ResponseEntity<McpResponseDTO> {
        val response = mcpInterface.executeTool(request)
        return ResponseEntity.ok(response)
    }
}
