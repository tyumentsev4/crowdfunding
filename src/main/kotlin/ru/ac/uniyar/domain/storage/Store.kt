package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.contract.openapi.OpenAPIJackson.asJsonObject
import org.http4k.contract.openapi.OpenAPIJackson.asPrettyJsonString
import org.http4k.contract.openapi.OpenAPIJackson.parse
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.isReadable

class Store(private val documentStoragePath: Path) {
    val projectsRepository: ProjectsRepository
    val entrepreneursRepository: EntrepreneursRepository
    val investmentsRepository: InvestmentsRepository

    val storeThread = thread(start = false) {
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

        entrepreneursRepository = if (node != null && node.has("entrepreneurs")) {
            EntrepreneursRepository.fromJson(node["entrepreneurs"])
        } else {
            EntrepreneursRepository()
        }

        investmentsRepository = if (node != null && node.has("investments")) {
            InvestmentsRepository.fromJson(node["investments"])
        } else {
            InvestmentsRepository()
        }
    }

    fun save() {
        val document: JsonNode =
            listOf(
                "projects" to projectsRepository.asJsonObject(),
                "entrepreneurs" to entrepreneursRepository.asJsonObject(),
                "investments" to investmentsRepository.asJsonObject()
            ).asJsonObject()
        val documentString = document.asPrettyJsonString()
        val file = documentStoragePath.toFile()
        file.writeText(documentString)
    }
}
