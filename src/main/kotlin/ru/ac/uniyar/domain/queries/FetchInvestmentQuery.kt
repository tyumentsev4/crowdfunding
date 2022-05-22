package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.*

class FetchInvestmentQuery(
    private val investmentsRepository: InvestmentsRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(id: UUID): InvestmentInfo {
        val investment = investmentsRepository.fetch(id) ?: throw InvestmentFetchError("Not found")
        val project = projectsRepository.fetch(investment.projectId) ?: throw ProjectFetchError("Not found")
        return InvestmentInfo(investment, project)
    }
}
