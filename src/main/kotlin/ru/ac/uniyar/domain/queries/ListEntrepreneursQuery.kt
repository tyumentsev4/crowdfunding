package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Store

class ListEntrepreneursQuery(store: Store) {
    private val entrepreneursRepository = store.entrepreneursRepository

    operator fun invoke() = entrepreneursRepository.list()
}
