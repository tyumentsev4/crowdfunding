package ru.ac.uniyar.domain.storage

import java.time.LocalDate

class Investments(val investments: List<Investment>) {
    companion object {
        private const val MAX_LENGTH_ON_PAGE = 5
    }

    private fun getAmount(): Double {
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

    fun isSuccessForecast(goal: Double, remainingDays: Int): Boolean {
        val days = mutableSetOf<LocalDate>()
        investments.forEach { investment ->
            days.add(investment.addTime.toLocalDate())
        }
        return getAmount() / days.size * remainingDays >= goal
    }
}
