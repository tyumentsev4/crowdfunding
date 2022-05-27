package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.contract.openapi.OpenAPIJackson.asJsonObject
import org.http4k.contract.openapi.OpenAPIJackson.asJsonValue
import java.util.*

val GUEST_ROLE_ID: UUID = UUID.fromString("09933a60-dac3-11ec-9d64-0242ac120002")
val REGISTERED_USER_ROLE_ID: UUID = UUID.fromString("1a1515e8-dac3-11ec-9d64-0242ac120002")
val ENTREPRENEUR_ROLE_ID: UUID = UUID.fromString("3be6ab64-dac3-11ec-9d64-0242ac120002")

data class RolePermissions(
    val id: UUID,
    val name: String,
    val showStartPage: Boolean,
    val showEntrepreneursList: Boolean,
    val showEntrepreneur: Boolean,
    val showProjectsList: Boolean,
    val showProject: Boolean,
    val canRegister: Boolean,
    val openNewProject: Boolean,
    val addInvestment: Boolean,
) {
    companion object {
        fun fromJson(jsonNode: JsonNode): RolePermissions {
            val jsonObject = jsonNode.asJsonObject()
            return RolePermissions(
                UUID.fromString(jsonObject["id"].asText()),
                jsonObject["name"].asText(),
                jsonObject["showStartPage"].asBoolean(),
                jsonObject["showEntrepreneursList"].asBoolean(),
                jsonObject["showEntrepreneur"].asBoolean(),
                jsonObject["showProjectsList"].asBoolean(),
                jsonObject["showProject"].asBoolean(),
                jsonObject["canRegister"].asBoolean(),
                jsonObject["openNewProject"].asBoolean(),
                jsonObject["addInvestment"].asBoolean(),
            )
        }

        val GUEST_ROLE = RolePermissions(
            id = GUEST_ROLE_ID,
            name = "Гость",
            showStartPage = true,
            showEntrepreneursList = true,
            showEntrepreneur = true,
            showProjectsList = true,
            showProject = true,
            canRegister = true,
            openNewProject = false,
            addInvestment = false
        )
    }

    fun asJsonObject(): JsonNode = listOf(
        "id" to id.toString().asJsonValue(),
        "name" to name.asJsonValue(),
        "showStartPage" to showStartPage.asJsonValue(),
        "showEntrepreneursList" to showEntrepreneursList.asJsonValue(),
        "showEntrepreneur" to showEntrepreneur.asJsonValue(),
        "showProjectsList" to showProjectsList.asJsonValue(),
        "showProject" to showProject.asJsonValue(),
        "canRegister" to canRegister.asJsonValue(),
        "openNewProject" to openNewProject.asJsonValue(),
        "addInvestment" to addInvestment.asJsonValue(),
    ).asJsonObject()
}
