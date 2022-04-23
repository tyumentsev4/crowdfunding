package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Store

class ListOpenProjectsQuery(store: Store) {
    private val projectsRepository = store.projectsRepository

    operator fun invoke() = projectsRepository.list().filter { it.isOpen() }
}
