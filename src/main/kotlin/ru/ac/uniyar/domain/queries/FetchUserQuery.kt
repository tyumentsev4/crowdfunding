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
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(id: UUID): UserInfo {
        val user = usersRepository.fetch(id) ?: throw UserFetchError("Not found")
        val investments = investmentsRepository.list().filter { it.investorName == user.name }
        val investmentsByProject = investments.associateBy { it.projectId }.toMutableMap()
        return UserInfo(user)
    }
}