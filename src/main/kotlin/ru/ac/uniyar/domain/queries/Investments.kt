package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment

class Investments(val investments: List<Investment>) {
    companion object {
        private const val MAX_LENGTH_ON_PAGE = 5
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
}
