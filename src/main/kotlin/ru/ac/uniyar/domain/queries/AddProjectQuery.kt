package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.domain.storage.REGISTERED_USER_ROLE_ID
import ru.ac.uniyar.domain.storage.UsersRepository
import ru.ac.uniyar.handlers.ProjectFromForm
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddProjectQuery(
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository
) {
    operator fun invoke(projectFromForm: ProjectFromForm): UUID {
        val user = usersRepository.fetch(projectFromForm.userId)!!
        if (user.roleId == REGISTERED_USER_ROLE_ID)
            usersRepository.update(user.copy(roleId = ENTREPRENEUR_ROLE_ID))

        if (projectFromForm.fundraisingEnd <= projectFromForm.fundraisingStart)
            throw StartTimeShouldBeLower()
        if (projectFromForm.fundSize <= 0)
            throw FundSizeShouldBePositiveInt()
        return projectsRepository.add(
            Project(
                EMPTY_UUID,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                projectFromForm.name,
                projectFromForm.userId,
                projectFromForm.description,
                projectFromForm.fundSize,
                projectFromForm.fundraisingStart,
                projectFromForm.fundraisingEnd
            )
        )
    }
}
