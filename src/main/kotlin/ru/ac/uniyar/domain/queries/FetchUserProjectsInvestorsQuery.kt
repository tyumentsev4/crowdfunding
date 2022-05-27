package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.*

class FetchUserProjectsInvestorsQuery(
    private val investmentRepository: InvestmentsRepository,
    private val projectsRepository: ProjectsRepository,
) {

    operator fun invoke(userId: UUID): List<ProjectInvestors> {
        val projects = projectsRepository.list().filter { it.entrepreneurId == userId }
        val projectsInvestors = mutableListOf<ProjectInvestors>()
        projects.forEach { project ->
            val investments =
                investmentRepository.list().filter { it.projectId == project.id }.filter { it.investorName != "" }
            projectsInvestors.add(
                ProjectInvestors(
                    project,
                    investments.map { InvestorInfo(it.investorName, it.contactInfo) }
                )
            )
        }
        return projectsInvestors
    }
}
