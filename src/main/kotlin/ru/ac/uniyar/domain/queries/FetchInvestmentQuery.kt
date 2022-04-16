package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Store
import java.util.UUID

class FetchInvestmentQuery(store: Store) {
    private val investmentsRepository = store.investmentsRepository
    private val projectsRepository = store.projectsRepository

    operator fun invoke(id: UUID): InvestmentInfo {
        val investment = investmentsRepository.fetch(id) ?: throw InvestmentFetchError("Not found")
        val project = projectsRepository.fetch(investment.projectId) ?: throw ProjectFetchError("Not found")
        return InvestmentInfo(investment, project)
    }
}

class InvestmentFetchError(message: String) : RuntimeException(message)
