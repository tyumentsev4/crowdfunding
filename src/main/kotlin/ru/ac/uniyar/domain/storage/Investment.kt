package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Investment(
    val id: UUID,
    val addTime: LocalDateTime,
    val projectId: UUID,
    val investorName: String,
    val contactInfo: String,
    val amount: Int
) {
    companion object {
        fun fromJson(node: JsonNode): Investment {
            val jsonObject = node.asJsonObject()
            return Investment(
                UUID.fromString(jsonObject["id"].asText()),
                LocalDateTime.parse(jsonObject["addTime"].asText(), DateTimeFormatter.ISO_DATE_TIME),
                UUID.fromString(jsonObject["projectId"].asText()),
                jsonObject["investorName"].asText(),
                jsonObject["contactInfo"].asText(),
                jsonObject["amount"].asInt()
            )
        }
    }

    fun asJsonObject(): JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "addTime" to addTime.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue(),
            "projectId" to projectId.toString().asJsonValue(),
            "investorName" to investorName.asJsonValue(),
            "contactInfo" to contactInfo.asJsonValue(),
            "amount" to amount.asJsonValue(),
        ).asJsonObject()
    }

    fun setId(uuid: UUID): Investment {
        return this.copy(id = uuid)
    }
}
