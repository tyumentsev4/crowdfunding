package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.domain.storage.UsersRepository
import java.util.UUID

class FetchProjectQuery(
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository,
    private val investmentRepository: InvestmentsRepository
) {

    operator fun invoke(id: UUID): ProjectInfo {
        val project = projectsRepository.fetch(id) ?: throw ProjectFetchError("Not found")
        val entrepreneur =
            usersRepository.fetch(project.entrepreneurId) ?: throw EntrepreneurFetchError("Not found")
        val investments = Investments(investmentRepository.list().filter { it.projectId == project.id })
        return ProjectInfo(
            project,
            entrepreneur,
            investments.lastInvestments(),
            investments.getSize(),
            investments.getNonAnonymousCount(),
            project.isSuccessful(),
            project.isSuccessForecast(),
            project.necessaryInvestments(),
            project.daysUntilTheEnd(),
            project.isOpen()
        )
    }
}
