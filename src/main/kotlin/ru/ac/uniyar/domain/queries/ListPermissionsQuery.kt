package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.RolePermissionsRepository

class ListPermissionsQuery(private val rolePermissionsRepository: RolePermissionsRepository) {

    operator fun invoke() = rolePermissionsRepository.list()
}