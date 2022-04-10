package ru.ac.uniyar.domain

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.UUID

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
        projectsMap[newId] = project.setUuid(newId)
        return newId
    }

    fun listProjects(
        page: Int = 0
    ): PagedResult<Project> {
        val list = projectsMap.values.toList()
        val pagedList = list.subListOrEmpty((page - 1) * PAGE_SIZE, page * PAGE_SIZE)

        return PagedResult(pagedList, countPageNumbers(list.size, PAGE_SIZE))
    }

    fun fetchAll(): Iterable<Project> {
        return projectsMap.values
    }
}
