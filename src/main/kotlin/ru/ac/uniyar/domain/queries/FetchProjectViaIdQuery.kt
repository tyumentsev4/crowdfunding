package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.*

class FetchProjectViaIdQuery(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(roleId: UUID) = projectsRepository.fetch(roleId)
}
