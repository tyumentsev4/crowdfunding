package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import ru.ac.uniyar.handlers.ProjectFromForm
import java.util.*

class ProjectsRepository(projects: Iterable<Project> = emptyList()) {
    private val projectsMap = projects.associateBy { it.id }.toMutableMap()

    companion object {
        fun fromJson(node: JsonNode): ProjectsRepository {
            val projects = node.map {
                Project.fromJson(it)
            }
            return ProjectsRepository(projects)
        }
    }

    fun asJsonObject(): JsonNode {
        return projectsMap.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID): Project? = projectsMap[id]

    fun add(project: Project): UUID {
        var newId = project.id
        while (projectsMap.containsKey(newId) || newId == EMPTY_UUID) {
            newId = UUID.randomUUID()
        }
        projectsMap[newId] = project.setId(newId)
        return newId
    }

    fun list() = projectsMap.values.toList()

    fun update(project: Project) {
        projectsMap[project.id] = project
    }

    fun delete(project: Project) {
        projectsMap.remove(project.id)
    }
}
