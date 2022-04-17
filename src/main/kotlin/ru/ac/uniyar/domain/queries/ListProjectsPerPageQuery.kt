package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.Store

class ListProjectsPerPageQuery(store: Store) {
    private val repository = store.projectsRepository

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        pageNumber: Int,
        fromFoundSize: Double?,
        toFoundSize: Double?,
        isOpen: Boolean?
    ): PagedResult<Project> {
        val baseFrom = fromFoundSize ?: Double.MIN_VALUE
        val baseTo = toFoundSize ?: Double.MAX_VALUE
        var list = repository.list().filter {
            it.fundSize in baseFrom..baseTo
        }
        if (isOpen != null) {
            list = list.filter { it.isOpen() == isOpen }
        }
        val subList = list.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(list.size, PAGE_SIZE))
    }
}
