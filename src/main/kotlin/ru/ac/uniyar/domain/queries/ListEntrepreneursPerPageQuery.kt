package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.EntrepreneursRepository
import java.time.LocalDateTime

class ListEntrepreneursPerPageQuery(private val entrepreneursRepository: EntrepreneursRepository) {

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        page: Int,
        fromDateTime: LocalDateTime?,
        toDateTime: LocalDateTime?,
        nameSearch: String?
    ): PagedResult<Entrepreneur> {
        val baseFrom = fromDateTime ?: LocalDateTime.MIN
        val baseTo = toDateTime ?: LocalDateTime.MAX
        var list = entrepreneursRepository.list().filter {
            it.addTime in baseFrom..baseTo
        }
        if (nameSearch != null) {
            list = list.filter { it.name.contains(nameSearch, ignoreCase = true) }
        }
        val entrepreneurList = list.subListOrEmpty((page - 1) * PAGE_SIZE, page * PAGE_SIZE)
        val pageCount = countPageNumbers(list.size, PAGE_SIZE)
        return PagedResult(entrepreneurList, pageCount)
    }
}
