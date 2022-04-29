package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import java.time.LocalDate

class Investments(val investments: List<Investment>) {
    companion object {
        private const val MAX_LENGTH_ON_PAGE = 5
    }

    private fun getAmount(): Int {
        return investments.sumOf { it.amount }
    }

    fun getCount(): Int {
        return investments.size
    }

    fun getNonAnonymousCount(): Int {
        return investments.filter { it.contactInfo != "" }.size
    }

    fun lastInvestments(): Iterable<Investment> {
        return if (investments.size > MAX_LENGTH_ON_PAGE)
            investments.subList(investments.size - MAX_LENGTH_ON_PAGE, investments.size)
        else investments
    }

    fun isSuccessForecast(goal: Int, remainingDays: Int): Boolean {
        val days = mutableSetOf<LocalDate>()
        investments.forEach { investment ->
            days.add(investment.addTime.toLocalDate())
        }
        if (days.size == 0)
            return false
        return getAmount() / days.size * remainingDays >= goal
    }
}
