package es.mango.pulse.switches.web.infrastructure.mcp

import es.mango.pulse.switches.web.infrastructure.mcp.model.McpRequestDTO
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpResponseDTO
import es.mango.pulse.switches.web.infrastructure.mcp.model.McpToolDTO
import es.mango.vulcan.controlcenter.web.infrastructure.rest.model.PageableWorkstation
import es.mango.vulcan.controlcenter.web.infrastructure.rest.model.Workstation
import org.springframework.http.ResponseEntity
import java.util.UUID

interface MortgageMcpInterface {

    fun listTools(): List<McpToolDTO>

    fun executeTool(request: McpRequestDTO): McpResponseDTO

    fun getWorkstations(xApiKey: String,page: Int?, size: Int?, sort: String?): ResponseEntity<PageableWorkstation>

    fun restartWorkstation(xApiKey: String,id: UUID):  ResponseEntity<Workstation>

}
