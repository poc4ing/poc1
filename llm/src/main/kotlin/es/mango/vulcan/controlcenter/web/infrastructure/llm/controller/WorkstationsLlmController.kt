package es.mango.pulse.switches.web.infrastructure.llm.controller


import es.mango.pulse.switches.web.infrastructure.llm.WorkstationsLlmAgentService
import es.mango.pulse.switches.web.infrastructure.llm.model.AgentInfoDTO
import es.mango.pulse.switches.web.infrastructure.llm.model.LlmChatRequestDTO
import es.mango.pulse.switches.web.infrastructure.llm.model.LlmChatResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/llm")
open class WorkstationsLlmController @Autowired constructor(
    private val agentService: WorkstationsLlmAgentService
) {

    @PostMapping("/chat")
    fun chat(@RequestBody request: LlmChatRequestDTO): ResponseEntity<LlmChatResponseDTO> {
        val response = agentService.processPrompt(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/agent/info")
    fun getAgentInfo(): ResponseEntity<AgentInfoDTO> {
        return ResponseEntity.ok(agentService.getAgentInfo())
    }
}
