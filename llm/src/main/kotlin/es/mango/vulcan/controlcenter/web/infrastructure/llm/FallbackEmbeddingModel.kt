package es.mango.pulse.switches.web.infrastructure.llm

import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.output.Response
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Modelo de embeddings en memoria y 100% JVM sin dependencias de librerías nativas C++ (ONNX Runtime / glibc).
 * Proporciona un fallback automático para entornos como contenedores Alpine Linux o agentes de CI/CD (Jenkins)
 * donde la librería dinámica 'ld-linux-x86-64.so.2' no está presente.
 */
open class FallbackEmbeddingModel : EmbeddingModel {

    companion object {
        private const val EMBEDDING_DIMENSION = 384

        private val GET_SWITCHES_KEYWORDS = setOf(
            "listar", "ver", "mostrar", "obtener", "disponibles", "toggles", "existentes", "list", "all"
        )
        private val TOGGLE_SWITCH_KEYWORDS = setOf(
            "alternar", "cambiar", "cambia", "activar", "desactivar", "invertir", "estado", "toggle"
        )
        private val FIND_CRITERIA_KEYWORDS = setOf(
            "buscar", "filtrar", "criterio", "texto", "coincida", "search", "criteria"
        )
        private val DELETE_SWITCH_KEYWORDS = setOf(
            "eliminar", "borrar", "remover", "delete"
        )
        private val CREATE_SWITCH_KEYWORDS = setOf(
            "crear", "alta", "añadir", "nuevo", "nueva", "create", "new"
        )
        private val GET_BY_ID_KEYWORDS = setOf(
            "id", "identificador", "uuid", "detalle"
        )
        private val GENERAL_SWITCH_KEYWORDS = setOf(
            "switch", "switches", "feature", "toggles", "toggle"
        )
    }

    override fun embed(text: String): Response<Embedding> {
        return Response.from(generateEmbedding(text))
    }

    override fun embed(textSegment: TextSegment): Response<Embedding> {
        return embed(textSegment.text())
    }

    override fun embedAll(textSegments: List<TextSegment>): Response<List<Embedding>> {
        val embeddings = textSegments.map { generateEmbedding(it.text()) }
        return Response.from(embeddings)
    }

    override fun dimension(): Int = EMBEDDING_DIMENSION

    private fun generateEmbedding(text: String): Embedding {
        val vector = FloatArray(EMBEDDING_DIMENSION)
        val normalized = text.lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9áéíóúñü\\s]"), " ")
        val tokens = normalized.split(Regex("\\s+")).filter { it.isNotBlank() }

        if (tokens.isEmpty()) {
            vector[0] = 1.0f
            return Embedding(vector)
        }

        for (token in tokens) {
            if (token in GET_SWITCHES_KEYWORDS) {
                for (i in 0..4) vector[i] += 2.0f
            }
            if (token in TOGGLE_SWITCH_KEYWORDS) {
                for (i in 10..14) vector[i] += 2.0f
            }
            if (token in FIND_CRITERIA_KEYWORDS) {
                for (i in 20..24) vector[i] += 2.0f
            }
            if (token in DELETE_SWITCH_KEYWORDS) {
                for (i in 30..34) vector[i] += 2.0f
            }
            if (token in CREATE_SWITCH_KEYWORDS) {
                for (i in 40..44) vector[i] += 2.0f
            }
            if (token in GET_BY_ID_KEYWORDS) {
                for (i in 50..54) vector[i] += 2.0f
            }
            if (token in GENERAL_SWITCH_KEYWORDS) {
                for (i in 60..64) vector[i] += 1.5f
            }

            val hashIdx = 70 + (abs(token.hashCode()) % 314)
            vector[hashIdx] += 1.0f
        }

        var sumSquares = 0.0
        for (v in vector) {
            sumSquares += (v * v)
        }
        val norm = sqrt(sumSquares).toFloat()
        if (norm > 0f) {
            for (i in vector.indices) {
                vector[i] /= norm
            }
        } else {
            vector[0] = 1.0f
        }

        return Embedding(vector)
    }
}
