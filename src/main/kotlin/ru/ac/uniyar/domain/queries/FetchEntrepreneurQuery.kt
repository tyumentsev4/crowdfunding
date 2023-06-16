package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.domain.storage.UsersRepository
import java.util.UUID

class FetchEntrepreneurQuery(
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(id: UUID): EntrepreneurInfo {
        val entrepreneur = usersRepository.fetch(id) ?: throw EntrepreneurFetchError("Not found")
        val hisProjects = projectsRepository.list()
            .filter { it.entrepreneurId == entrepreneur.id }
            .sortedWith(compareBy(Project::fundraisingEnd, Project::fundraisingStart)).reversed()
        return EntrepreneurInfo(entrepreneur, hisProjects)
    }
}
