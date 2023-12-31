package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository

class ListProjectsPerPageQuery(private val projectsRepository: ProjectsRepository) {

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        pageNumber: Int,
        fromFoundSize: Int?,
        toFoundSize: Int?,
        isOpen: Boolean?
    ): PagedResult<Project> {
        val baseFrom = fromFoundSize ?: Int.MIN_VALUE
        val baseTo = toFoundSize ?: Int.MAX_VALUE
        var list = projectsRepository.list().filter {
            it.fundSize in baseFrom..baseTo
        }
        if (isOpen != null) {
            list = list.filter { it.isOpen() == isOpen }
        }
        val subList = list.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(list.size, PAGE_SIZE))
    }
}
