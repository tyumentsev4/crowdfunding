package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.*

class ListUserProjectsPerPageQuery(
    private val projectsRepository: ProjectsRepository,
) {

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        pageNumber: Int,
        userId: UUID
    ): PagedResult<Project> {
        val list = projectsRepository.list().filter {
            it.entrepreneurId == userId
        }
        val subList = list.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(list.size, PAGE_SIZE))
    }
}