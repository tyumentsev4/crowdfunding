package ru.ac.uniyar.domain

class Investments(val investments: List<Investment>) {
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
}
