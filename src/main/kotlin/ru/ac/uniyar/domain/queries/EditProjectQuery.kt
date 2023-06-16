package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.handlers.ProjectFromForm
import java.util.UUID

class EditProjectQuery(
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(projectId: UUID, projectFromForm: ProjectFromForm) {
        if (projectFromForm.fundraisingEnd <= projectFromForm.fundraisingStart)
            throw StartTimeShouldBeLower()
        if (projectFromForm.fundSize <= 0)
            throw FundSizeShouldBePositiveInt()
        val project = projectsRepository.fetch(projectId)!!
        projectsRepository.update(
            project.copy(
                name = projectFromForm.name,
                description = projectFromForm.description,
                fundSize = projectFromForm.fundSize,
                fundraisingStart = projectFromForm.fundraisingStart,
                fundraisingEnd = projectFromForm.fundraisingEnd
            )
        )
    }
}
