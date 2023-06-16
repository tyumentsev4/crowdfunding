package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.ProjectSortSettings
import ru.ac.uniyar.domain.storage.ProjectsRepository
import ru.ac.uniyar.domain.storage.UsersRepository
import java.util.UUID

class FetchUserQuery(
    private val usersRepository: UsersRepository,
    private val projectsRepository: ProjectsRepository,
    private val investmentsRepository: InvestmentsRepository,
) {
    operator fun invoke(id: UUID, sortSettings: ProjectSortSettings): UserInfo {
        val user = usersRepository.fetch(id) ?: throw UserFetchError("Not found")
        val investments = investmentsRepository.list().filter { it.investorId == id }
        val projects = projectsRepository.list()
        val projectsSorted = when (sortSettings) {
            ProjectSortSettings.OpenFirst -> projects.sortedByDescending { it.isOpen() }
            ProjectSortSettings.CloseFirst -> projects.sortedBy { it.isOpen() }
            ProjectSortSettings.DateEndInc -> projects.sortedBy { it.fundraisingEnd }
            ProjectSortSettings.DateEndDec -> projects.sortedByDescending { it.fundraisingEnd }
            else -> { projects }
        }
        val projectsInvestments = mutableListOf<ProjectInvestments>()
        projectsSorted.forEach { project ->
            val projectInvestments = investments.filter { it.projectId == project.id }
            if (projectInvestments.isNotEmpty())
                projectsInvestments.add(
                    ProjectInvestments(
                        project,
                        projectInvestments
                    )
                )
        }
        return UserInfo(user, projectsInvestments)
    }
}
