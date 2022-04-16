package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Store

class ListProjectsQuery(store: Store) {
    private val projectsRepository = store.projectsRepository

    operator fun invoke() = projectsRepository.list()
}
