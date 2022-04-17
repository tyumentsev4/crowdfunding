package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Store
import java.time.LocalDateTime

class ListInvestmentsPerPageQuery(store: Store) {
    private val repository = store.investmentsRepository

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        pageNumber: Int,
        fromDateTime: LocalDateTime?,
        toDateTime: LocalDateTime?,
        fromAmount: Double?,
        toAmount: Double?
    ): PagedResult<Investment> {
        val baseFromDate = fromDateTime ?: LocalDateTime.MIN
        val baseToDate = toDateTime ?: LocalDateTime.MAX
        val baseFromAmount = fromAmount ?: Double.MIN_VALUE
        val baseToAmount = toAmount ?: Double.MAX_VALUE
        val list = repository.list().filter {
            it.addTime in baseFromDate..baseToDate &&
                it.amount in baseFromAmount..baseToAmount
        }
        val subList = list.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(list.size, PAGE_SIZE))
    }
}
