package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Store
import java.util.UUID

class FetchProjectQuery(store: Store) {
    private val projectsRepository = store.projectsRepository
    private val entrepreneursRepository = store.entrepreneursRepository
    private val investmentRepository = store.investmentsRepository

    operator fun invoke(id: UUID): ProjectInfo {
        val project = projectsRepository.fetch(id) ?: throw ProjectFetchError("Not found")
        val entrepreneur =
            entrepreneursRepository.fetch(project.entrepreneurId) ?: throw EntrepreneurFetchError("Not found")
        val investments = Investments(investmentRepository.list().filter { it.projectId == project.id })
        return ProjectInfo(project, entrepreneur, investments)
    }
}
