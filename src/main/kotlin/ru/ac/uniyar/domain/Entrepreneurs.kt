package ru.ac.uniyar.domain

class Entrepreneurs {
    fun add(entrepreneur: Entrepreneur) {
        entrepreneursList.add(entrepreneur)
    }

    fun size(): Int {
        return entrepreneursList.size
    }

    fun getAll(): Iterable<Entrepreneur> {
        return entrepreneursList
    }

    fun fetch(index: Int): Entrepreneur? {
        return entrepreneursList.getOrNull(index)
    }
    private val entrepreneursList = mutableListOf<Entrepreneur>()
}