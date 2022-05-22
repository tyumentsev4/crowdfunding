package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.domain.storage.UsersRepository
import java.util.*

class FetchUserViaUserId(
    private val workerRepository: UsersRepository
) {
    operator fun invoke(token: String): User? {
        val uuid = try {
            UUID.fromString(token)
        } catch (_: java.lang.IllegalArgumentException) {
            return null
        }
        return workerRepository.fetch(uuid)
    }
}