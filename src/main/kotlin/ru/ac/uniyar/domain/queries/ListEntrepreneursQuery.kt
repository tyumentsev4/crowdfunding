package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EntrepreneursRepository

class ListEntrepreneursQuery(private val entrepreneursRepository: EntrepreneursRepository) {
    operator fun invoke() = entrepreneursRepository.list()
}
