package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.contract.openapi.OpenAPIJackson.asJsonObject
import org.http4k.contract.openapi.OpenAPIJackson.asJsonValue
import java.util.UUID

val GUEST_ROLE_ID: UUID = UUID.fromString("09933a60-dac3-11ec-9d64-0242ac120002")
val REGISTERED_USER_ROLE_ID: UUID = UUID.fromString("1a1515e8-dac3-11ec-9d64-0242ac120002")
val ENTREPRENEUR_ROLE_ID: UUID = UUID.fromString("3be6ab64-dac3-11ec-9d64-0242ac120002")

data class RolePermissions(
    val id: UUID,
    val name: String,
    val seeEntrepreneursList: Boolean,
    val seeEntrepreneurInfo: Boolean,
    val seeProjectsList: Boolean,
    val seeProjectInfo: Boolean,
    val openNewProject: Boolean,
    val addInvestment: Boolean,
    val seeUserProjectsList: Boolean,
    val editProject: Boolean,
    val closeProject: Boolean,
    val deleteProject: Boolean,
    val seeUserInvestorsList: Boolean
) {
    companion object {
        fun fromJson(jsonNode: JsonNode): RolePermissions {
            val jsonObject = jsonNode.asJsonObject()
            return RolePermissions(
                UUID.fromString(jsonObject["id"].asText()),
                jsonObject["name"].asText(),
                jsonObject["seeEntrepreneursList"].asBoolean(),
                jsonObject["seeEntrepreneurInfo"].asBoolean(),
                jsonObject["seeProjectsList"].asBoolean(),
                jsonObject["seeProjectInfo"].asBoolean(),
                jsonObject["openNewProject"].asBoolean(),
                jsonObject["addInvestment"].asBoolean(),
                jsonObject["seeUserProjectsList"].asBoolean(),
                jsonObject["editProject"].asBoolean(),
                jsonObject["closeProject"].asBoolean(),
                jsonObject["deleteProject"].asBoolean(),
                jsonObject["seeUserInvestorsList"].asBoolean(),
            )
        }

        val GUEST_ROLE = RolePermissions(
            id = GUEST_ROLE_ID,
            name = "Гость",
            seeEntrepreneursList = true,
            seeEntrepreneurInfo = true,
            seeProjectsList = true,
            seeProjectInfo = true,
            openNewProject = false,
            addInvestment = false,
            seeUserProjectsList = false,
            editProject = false,
            closeProject = false,
            deleteProject = false,
            seeUserInvestorsList = false
        )
    }

    fun asJsonObject(): JsonNode = listOf(
        "id" to id.toString().asJsonValue(),
        "name" to name.asJsonValue(),
        "seeEntrepreneursList" to seeEntrepreneursList.asJsonValue(),
        "seeEntrepreneurInfo" to seeEntrepreneurInfo.asJsonValue(),
        "seeProjectsList" to seeProjectsList.asJsonValue(),
        "seeProjectInfo" to seeProjectInfo.asJsonValue(),
        "openNewProject" to openNewProject.asJsonValue(),
        "addInvestment" to addInvestment.asJsonValue(),
        "seeUserProjectsList" to seeUserProjectsList.asJsonValue(),
        "editProject" to editProject.asJsonValue(),
        "closeProject" to closeProject.asJsonValue(),
        "deleteProject" to deleteProject.asJsonValue(),
        "seeUserInvestorsList" to seeUserInvestorsList.asJsonValue(),
    ).asJsonObject()
}
