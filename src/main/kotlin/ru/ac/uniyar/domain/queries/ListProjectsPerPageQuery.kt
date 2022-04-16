package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.Store

class ListProjectsPerPageQuery(store: Store) {
    private val repository = store.projectsRepository

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(pageNumber: Int): PagedResult<Project> {
        val list = repository.list()
        val subList = list.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(list.size, PAGE_SIZE))
    }
}
