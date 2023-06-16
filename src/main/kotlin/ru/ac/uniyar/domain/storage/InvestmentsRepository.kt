package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.UUID

class InvestmentsRepository(investments: Iterable<Investment> = emptyList()) {
    private val investmentsMap = investments.associateBy { it.id }.toMutableMap()

    companion object {
        fun fromJson(node: JsonNode): InvestmentsRepository {
            val investments = node.map {
                Investment.fromJson(it)
            }
            return InvestmentsRepository(investments)
        }
    }

    fun asJsonObject(): JsonNode {
        return investmentsMap.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID): Investment? = investmentsMap[id]

    fun add(investment: Investment): UUID {
        var newId = investment.id
        while (investmentsMap.containsKey(newId) || newId == EMPTY_UUID) {
            newId = UUID.randomUUID()
        }
        investmentsMap[newId] = investment.setId(newId)
        return newId
    }

    fun list() = investmentsMap.values.toList()
}
