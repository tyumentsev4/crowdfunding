package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Entrepreneur(
    val id: UUID,
    val name: String,
    val addTime: LocalDateTime
) {
    companion object {
        fun fromJson(node: JsonNode): Entrepreneur {
            val jsonObject = node.asJsonObject()
            return Entrepreneur(
                UUID.fromString(jsonObject["id"].asText()),
                jsonObject["name"].asText(),
                LocalDateTime.parse(jsonObject["addTime"].asText(), DateTimeFormatter.ISO_DATE_TIME)
            )
        }
    }

    fun asJsonObject(): JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "name" to name.asJsonValue(),
            "addTime" to addTime.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue()
        ).asJsonObject()
    }

    fun setId(uuid: UUID): Entrepreneur {
        return this.copy(id = uuid)
    }
}
