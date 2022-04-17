package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Store
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class AddInvestmentQuery(store: Store) {
    private val investmentsRepository = store.investmentsRepository
    private val projectsRepository = store.projectsRepository

    operator fun invoke(
        projectId: UUID,
        investorName: String,
        contactInfo: String,
        amount: Int
    ): UUID {
        val investment = Investment(
            EMPTY_UUID,
            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
            projectId,
            investorName,
            contactInfo,
            amount
        )
        val project = projectsRepository.fetch(investment.projectId) ?: throw ProjectFetchError("Not found")
        projectsRepository.update(project.copy(collectedAmount = project.collectedAmount + investment.amount))
        return investmentsRepository.add(investment)
    }
}
