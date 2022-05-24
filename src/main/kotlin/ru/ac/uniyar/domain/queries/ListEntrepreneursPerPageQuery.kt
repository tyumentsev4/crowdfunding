package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.domain.storage.UsersRepository
import java.time.LocalDateTime

class ListEntrepreneursPerPageQuery(private val usersRepository: UsersRepository) {

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        page: Int,
        fromDateTime: LocalDateTime?,
        toDateTime: LocalDateTime?,
        nameSearch: String?,
    ): PagedResult<User> {
        val baseFrom = fromDateTime ?: LocalDateTime.MIN
        val baseTo = toDateTime ?: LocalDateTime.MAX
        var list = usersRepository.list()
            .filter { it.roleId == ENTREPRENEUR_ROLE_ID }
            .filter { it.addTime in baseFrom..baseTo }
        if (nameSearch != null) {
            list = list.filter { it.name.contains(nameSearch, ignoreCase = true) }
        }
        val entrepreneurList = list.subListOrEmpty((page - 1) * PAGE_SIZE, page * PAGE_SIZE)
        val pageCount = countPageNumbers(list.size, PAGE_SIZE)
        return PagedResult(entrepreneurList, pageCount)
    }
}
