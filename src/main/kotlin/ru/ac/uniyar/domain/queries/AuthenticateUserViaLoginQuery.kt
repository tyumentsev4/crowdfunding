package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.queries.computations.hashPassword
import ru.ac.uniyar.domain.storage.Settings
import ru.ac.uniyar.domain.storage.UsersRepository

class AuthenticateUserViaLoginQuery(
    private val usersRepository: UsersRepository,
    private val settings: Settings
) {
    operator fun invoke(name: String, password: String): String {
        val user = usersRepository.list().find { it.name == name } ?: throw AuthenticationError()
        val hashedPassword = hashPassword(password, settings.salt)
        if (hashedPassword != user.password)
            throw AuthenticationError()
        return user.id.toString()
    }
}

class AuthenticationError : RuntimeException("User or password is wrong")
