package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.RolePermissionsRepository
import java.util.UUID

class FetchPermissionsViaIdQuery(
    private val permissionsRepository: RolePermissionsRepository
) {
    operator fun invoke(roleId: UUID) = permissionsRepository.fetch(roleId)
}
