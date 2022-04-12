package ru.ac.uniyar.domain

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID

data class Project(
    val id: UUID,
    val addTime: LocalDateTime,
    val name: String,
    val entrepreneurId: UUID,
    val description: String,
    val fundSize: Double,
    val fundraisingStart: LocalDateTime,
    val fundraisingEnd: LocalDateTime,
    val collectedAmount: Double = 0.0
) {
    companion object {
        fun fromJson(node: JsonNode): Project {
            val jsonObject = node.asJsonObject()
            return Project(
                UUID.fromString(jsonObject["id"].asText()),
                LocalDateTime.parse(jsonObject["addTime"].asText(), DateTimeFormatter.ISO_DATE_TIME),
                jsonObject["name"].asText(),
                UUID.fromString(jsonObject["entrepreneurId"].asText()),
                jsonObject["description"].asText(),
                jsonObject["fundSize"].asDouble(),
                LocalDateTime.parse(jsonObject["fundraisingStart"].asText(), DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse(jsonObject["fundraisingEnd"].asText(), DateTimeFormatter.ISO_DATE_TIME),
                jsonObject["collectedAmount"].asDouble()
            )
        }
    }

    fun asJsonObject(): JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "addTime" to addTime.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue(),
            "name" to name.asJsonValue(),
            "entrepreneurId" to entrepreneurId.toString().asJsonValue(),
            "description" to description.asJsonValue(),
            "fundSize" to fundSize.asJsonObject(),
            "fundraisingStart" to fundraisingStart.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue(),
            "fundraisingEnd" to fundraisingEnd.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue(),
            "collectedAmount" to collectedAmount.asJsonValue()
        ).asJsonObject()
    }

    fun setUuid(uuid: UUID): Project {
        return this.copy(id = uuid)
    }

    fun isOpen(): Boolean {
        return (fundraisingEnd > LocalDateTime.now()) && (fundraisingStart <= LocalDateTime.now())
    }

    fun daysUntilTheEnd(): Int {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), fundraisingEnd).toInt()
    }

    fun isSuccessful(): Boolean? {
        if (isOpen())
            return null
        return collectedAmount >= fundSize
    }

    fun necessaryInvestments(): Double {
        if (fundSize < collectedAmount)
            return 0.0
        return fundSize - collectedAmount
    }
}
