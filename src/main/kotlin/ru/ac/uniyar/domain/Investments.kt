package ru.ac.uniyar.domain

import java.time.LocalDate

class Investments(val investments: List<Investment>) {
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
        return if (investments.size > 5)
            investments.subList(investments.size - 5, investments.size)
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
