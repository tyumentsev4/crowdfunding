package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class CloseProjectQuery(
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(project: Project) {
        projectsRepository.update(project.copy(fundraisingEnd = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
    }
}
