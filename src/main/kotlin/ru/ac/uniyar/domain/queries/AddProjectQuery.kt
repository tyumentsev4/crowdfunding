package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.EntrepreneursRepository
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.handlers.ProjectFromForm
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddProjectQuery(
    private val projectsRepository: ProjectsRepository,
    private val entrepreneursRepository: EntrepreneursRepository,
) {
    @Suppress("ThrowsCount")
    operator fun invoke(projectFromForm: ProjectFromForm): UUID {
        if (entrepreneursRepository.fetch(projectFromForm.entrepreneurId) == null)
            throw EntrepreneurNotFoundError()
        if (projectFromForm.fundraisingEnd <= projectFromForm.fundraisingStart)
            throw StartTimeShouldBeLower()
        if (projectFromForm.fundSize <= 0)
            throw FundSizeShouldBePositiveInt()
        return projectsRepository.add(
            Project(
                EMPTY_UUID,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                projectFromForm.name,
                projectFromForm.entrepreneurId,
                projectFromForm.description,
                projectFromForm.fundSize,
                projectFromForm.fundraisingStart,
                projectFromForm.fundraisingEnd
            )
        )
    }
}
