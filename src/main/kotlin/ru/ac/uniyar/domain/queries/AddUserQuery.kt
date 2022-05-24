package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.queries.computations.hashPassword
import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.REGISTERED_USER_ROLE_ID
import ru.ac.uniyar.domain.storage.Settings
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.domain.storage.UsersRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddUserQuery(
    private val usersRepository: UsersRepository,
    private val settings: Settings,
) {

    operator fun invoke(
        name: String,
        password: String,
        contactInfo: String
    ): UUID {
        val hashedPassword = hashPassword(password, settings.salt)
        return usersRepository.add(
            User(
                EMPTY_UUID,
                REGISTERED_USER_ROLE_ID,
                name,
                hashedPassword,
                contactInfo,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            )
        )
    }
}
