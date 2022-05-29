package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddInvestmentQuery(
    private val investmentsRepository: InvestmentsRepository,
    private val projectsRepository: ProjectsRepository,
) {
    operator fun invoke(
        projectId: UUID,
        investorId: UUID,
        investorName: String,
        contactInfo: String,
        amount: Int
    ): UUID {
        if (projectsRepository.fetch(projectId) == null)
            throw ProjectNotFound()
        if (amount <= 0)
            throw AmountShouldBePositiveInt()
        val investment = Investment(
            EMPTY_UUID,
            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
            projectId,
            investorId,
            investorName,
            contactInfo,
            amount
        )
        val project = projectsRepository.fetch(investment.projectId)!!
        projectsRepository.update(project.copy(collectedAmount = project.collectedAmount + investment.amount))
        return investmentsRepository.add(investment)
    }
}
