package es.mango.pulse.switches.web.infrastructure.llm

import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["es.mango.pulse.switches.web.infrastructure.llm"])
open class LlmConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    open fun embeddingModel(): EmbeddingModel {
        return try {
            AllMiniLmL6V2EmbeddingModel()
        } catch (t: Throwable) {
            log.warn(
                "No se pudo cargar la librería nativa de ONNX Runtime ({}: {}). Activando FallbackEmbeddingModel en memoria.",
                t.javaClass.simpleName,
                t.message
            )
            FallbackEmbeddingModel()
        }
    }
}
