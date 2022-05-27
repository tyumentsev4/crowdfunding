package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.domain.storage.UsersRepository
import java.util.*

class FetchUserQuery(
    private val usersRepository: UsersRepository,
    private val investmentsRepository: InvestmentsRepository,
) {
    operator fun invoke(id: UUID): UserInfo {
        val user = usersRepository.fetch(id) ?: throw UserFetchError("Not found")
        return UserInfo(user)
    }
}