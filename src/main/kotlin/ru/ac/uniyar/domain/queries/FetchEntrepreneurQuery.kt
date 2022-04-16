package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Store
import java.util.UUID

class FetchEntrepreneurQuery(store: Store) {
    private val entrepreneursRepository = store.entrepreneursRepository
    private val projectsRepository = store.projectsRepository

    operator fun invoke(id: UUID): EntrepreneurInfo {
        val entrepreneur = entrepreneursRepository.fetch(id) ?: throw EntrepreneurFetchError("Not found")
        val hisProjects = projectsRepository.list()
            .filter { it.entrepreneurId == entrepreneur.id }
            .sortedByDescending { it.fundraisingStart }
        return EntrepreneurInfo(entrepreneur, hisProjects)
    }
}

class EntrepreneurFetchError(message: String) : RuntimeException(message)
