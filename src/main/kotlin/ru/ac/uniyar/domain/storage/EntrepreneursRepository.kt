package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.UUID

class EntrepreneursRepository(entrepreneurs: Iterable<Entrepreneur> = emptyList()) {
    private val entrepreneursMap = entrepreneurs.associateBy { it.id }.toMutableMap()

    companion object {
        fun fromJson(node: JsonNode): EntrepreneursRepository {
            val entrepreneurs = node.map {
                Entrepreneur.fromJson(it)
            }
            return EntrepreneursRepository(entrepreneurs)
        }
    }

    fun asJsonObject(): JsonNode {
        return entrepreneursMap.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID): Entrepreneur? = entrepreneursMap[id]

    fun add(entrepreneur: Entrepreneur): UUID {
        var newId = entrepreneur.id
        while (entrepreneursMap.containsKey(newId) || newId == EMPTY_UUID) {
            newId = UUID.randomUUID()
        }
        entrepreneursMap[newId] = entrepreneur.setId(newId)
        return newId
    }

    fun list() = entrepreneursMap.values.toList()
}
