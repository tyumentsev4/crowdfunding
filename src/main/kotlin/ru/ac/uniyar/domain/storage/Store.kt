package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.contract.openapi.OpenAPIJackson.asJsonObject
import org.http4k.contract.openapi.OpenAPIJackson.asPrettyJsonString
import org.http4k.contract.openapi.OpenAPIJackson.parse
import ru.ac.uniyar.domain.ENTREPRENEUR_ROLE
import ru.ac.uniyar.domain.REGISTERED_USER_ROLE
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.isReadable

class Store(private val documentStoragePath: Path) {
    val rolePermissionsRepository: RolePermissionsRepository
    val projectsRepository: ProjectsRepository
    val usersRepository: UsersRepository
    val investmentsRepository: InvestmentsRepository

    private val storeThread = thread(start = false) {
        save()
    }

    init {
        Runtime.getRuntime().addShutdownHook(storeThread)
        val node = if (documentStoragePath.isReadable()) {
            val file = documentStoragePath.toFile()
            val jsonDocument = file.readText()
            parse(jsonDocument)
        } else null

        projectsRepository = if (node != null && node.has("projects")) {
            ProjectsRepository.fromJson(node["projects"])
        } else {
            ProjectsRepository()
        }

        usersRepository = if (node != null && node.has("users")) {
            UsersRepository.fromJson(node["users"])
        } else {
            UsersRepository()
        }

        investmentsRepository = if (node != null && node.has("investments")) {
            InvestmentsRepository.fromJson(node["investments"])
        } else {
            InvestmentsRepository()
        }

        rolePermissionsRepository = if (node != null && node.has("rolePermissions")) {
            RolePermissionsRepository.fromJson(node["rolePermissions"])
        } else {
            RolePermissionsRepository()
        }

        if (node == null) {
            rolePermissionsRepository.add(REGISTERED_USER_ROLE)
            rolePermissionsRepository.add(ENTREPRENEUR_ROLE)
        }
    }

    fun save() {
        val document: JsonNode =
            listOf(
                "rolePermissions" to rolePermissionsRepository.asJsonObject(),
                "projects" to projectsRepository.asJsonObject(),
                "users" to usersRepository.asJsonObject(),
                "investments" to investmentsRepository.asJsonObject()
            ).asJsonObject()
        val documentString = document.asPrettyJsonString()
        val file = documentStoragePath.toFile()
        file.writeText(documentString)
    }
}
