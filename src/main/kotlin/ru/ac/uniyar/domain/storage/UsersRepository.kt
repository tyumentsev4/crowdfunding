package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.UUID

class UsersRepository(entrepreneurs: Iterable<User> = emptyList()) {
    private val usersMap = entrepreneurs.associateBy { it.id }.toMutableMap()

    companion object {
        fun fromJson(node: JsonNode): UsersRepository {
            val users = node.map {
                User.fromJson(it)
            }
            return UsersRepository(users)
        }
    }

    fun asJsonObject(): JsonNode {
        return usersMap.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID): User? = usersMap[id]

    fun add(user: User): UUID {
        var newId = user.id
        while (usersMap.containsKey(newId) || newId == EMPTY_UUID) {
            newId = UUID.randomUUID()
        }
        usersMap[newId] = user.setId(newId)
        return newId
    }

    fun list() = usersMap.values.toList()

    fun update(user: User) {
        usersMap[user.id] = user
    }
}
