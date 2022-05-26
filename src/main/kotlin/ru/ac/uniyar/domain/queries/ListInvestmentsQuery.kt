package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository

class ListInvestmentsQuery(private val investmentsRepository: InvestmentsRepository) {
    operator fun invoke() = investmentsRepository.list()
}