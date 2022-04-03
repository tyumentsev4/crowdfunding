package ru.ac.uniyar.domain

class Investments {
    fun add(investment: Investment) {
        investmentsList.add(investment)
    }

    fun size(): Int {
        return investmentsList.size
    }

    fun getAll(): Iterable<Investment> {
        return investmentsList
    }

    fun fetch(index: Int): Investment? {
        return investmentsList.getOrNull(index)
    }
    private val investmentsList = mutableListOf<Investment>()
}