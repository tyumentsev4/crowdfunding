package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.ProjectSortSettings
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.domain.storage.UsersRepository

class ChangeUserProjectSortQuery(
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(user: User, projectSortSettings: ProjectSortSettings) {
        usersRepository.update(
            user.copy(sortSettings = projectSortSettings)
        )
    }
}
