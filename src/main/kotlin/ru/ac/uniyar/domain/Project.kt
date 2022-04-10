package ru.ac.uniyar.domain

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Project(
    val id: UUID,
    val addTime: LocalDateTime,
    val name: String,
    val entrepreneur: String,
    val description: String,
    val fundSize: Int,
    val fundraisingStart: LocalDateTime,
    val fundraisingEnd: LocalDateTime,
) {
    companion object {
        fun fromJson(node: JsonNode): Project {
            val jsonObject = node.asJsonObject()
            return Project(
                UUID.fromString(jsonObject["id"].asText()),
                LocalDateTime.parse(jsonObject["addTime"].asText(), DateTimeFormatter.ISO_DATE_TIME),
                jsonObject["name"].asText(),
                jsonObject["entrepreneur"].asText(),
                jsonObject["description"].asText(),
                jsonObject["fundSize"].asInt(),
                LocalDateTime.parse(jsonObject["fundraisingStart"].asText(), DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse(jsonObject["fundraisingStart"].asText(), DateTimeFormatter.ISO_DATE_TIME),
            )
        }
    }

    fun asJsonObject(): JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "addTime" to addTime.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue(),
            "name" to name.asJsonValue(),
            "entrepreneur" to entrepreneur.asJsonValue(),
            "description" to description.asJsonValue(),
            "fundSize" to fundSize.asJsonObject(),
            "fundraisingStart" to fundraisingStart.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue(),
            "fundraisingEnd" to fundraisingEnd.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue()
        ).asJsonObject()
    }

    fun setUuid(uuid: UUID): Project {
        return this.copy(id = uuid)
    }
}
