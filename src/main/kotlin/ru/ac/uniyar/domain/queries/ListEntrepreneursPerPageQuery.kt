package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.Store

class ListEntrepreneursPerPageQuery(store: Store) {
    private val entrepreneursRepository = store.entrepreneursRepository

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(page: Int): PagedResult<Entrepreneur> {
        val list = entrepreneursRepository.list()

        val entrepreneurList = list.subListOrEmpty((page - 1) * PAGE_SIZE, page * PAGE_SIZE)
        val pageCount = countPageNumbers(list.size, PAGE_SIZE)
        return PagedResult(entrepreneurList, pageCount)
    }
}
