package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.Store
import java.util.UUID

class FetchEntrepreneurQuery(store: Store) {
    private val entrepreneursRepository = store.entrepreneursRepository
    private val projectsRepository = store.projectsRepository

    operator fun invoke(id: UUID): EntrepreneurInfo {
        val entrepreneur = entrepreneursRepository.fetch(id) ?: throw EntrepreneurFetchError("Not found")
        val hisProjects = projectsRepository.list()
            .filter { it.entrepreneurId == entrepreneur.id }
            .sortedWith(compareBy(Project::fundraisingEnd, Project::fundraisingStart)).reversed()
        return EntrepreneurInfo(entrepreneur, hisProjects)
    }
}

class EntrepreneurFetchError(message: String) : RuntimeException(message)
