package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class User(
    val id: UUID,
    val roleId: UUID,
    val name: String,
    val password: String,
    val contactInfo: String,
    val addTime: LocalDateTime
) {
    companion object {
        fun fromJson(node: JsonNode): User {
            val jsonObject = node.asJsonObject()
            return User(
                UUID.fromString(jsonObject["id"].asText()),
                UUID.fromString(jsonObject["roleId"].asText()),
                jsonObject["name"].asText(),
                jsonObject["password"].asText(),
                jsonObject["contactInfo"].asText(),
                LocalDateTime.parse(jsonObject["addTime"].asText(), DateTimeFormatter.ISO_DATE_TIME)
            )
        }
    }

    fun asJsonObject(): JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "roleId" to roleId.toString().asJsonValue(),
            "name" to name.asJsonValue(),
            "password" to password.asJsonValue(),
            "contactInfo" to contactInfo.asJsonValue(),
            "addTime" to addTime.format(DateTimeFormatter.ISO_DATE_TIME).asJsonValue()
        ).asJsonObject()
    }

    fun setId(uuid: UUID): User {
        return this.copy(id = uuid)
    }

    fun setRoleId(uuid: UUID): User {
        return this.copy(roleId = uuid)
    }
}
