package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.*

class DeleteProjectQuery(
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(project: Project) {
        projectsRepository.delete(project)
    }
}
