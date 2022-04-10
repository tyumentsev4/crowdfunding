package ru.ac.uniyar.domain

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
        investmentsMap[newId] = investment.setUuid(newId)
        return newId
    }

    fun listInvestments(
        page: Int = 0
    ): PagedResult<Investment> {
        val list = investmentsMap.values.toList()
        val pagedList = list.subListOrEmpty((page - 1) * PAGE_SIZE, page * PAGE_SIZE)

        return PagedResult(pagedList, countPageNumbers(list.size, PAGE_SIZE))
    }

    fun fetchAll(): Iterable<Investment> {
        return investmentsMap.values
    }
}
