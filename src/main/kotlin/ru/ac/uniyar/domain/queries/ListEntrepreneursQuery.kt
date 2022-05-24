package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.UsersRepository

class ListEntrepreneursQuery(private val usersRepository: UsersRepository) {
    operator fun invoke() = usersRepository.list().filter { it.roleId == ENTREPRENEUR_ROLE_ID }
}
