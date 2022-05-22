package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EntrepreneursRepository
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.*

class FetchEntrepreneurQuery(
    private val entrepreneursRepository: EntrepreneursRepository,
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(id: UUID): EntrepreneurInfo {
        val entrepreneur = entrepreneursRepository.fetch(id) ?: throw EntrepreneurFetchError("Not found")
        val hisProjects = projectsRepository.list()
            .filter { it.entrepreneurId == entrepreneur.id }
            .sortedWith(compareBy(Project::fundraisingEnd, Project::fundraisingStart)).reversed()
        return EntrepreneurInfo(entrepreneur, hisProjects)
    }
}
