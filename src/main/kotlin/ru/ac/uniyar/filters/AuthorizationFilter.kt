package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.queries.FetchPermissionsViaIdQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.User

fun authorizationFilter(
    currentWorker: RequestContextLens<User?>,
    permissionsLens: RequestContextLens<RolePermissions>,
    fetchPermissionViaIdQuery: FetchPermissionsViaIdQuery
): Filter = Filter { next: HttpHandler ->
    { request: Request ->
        val permissions = currentWorker(request)?.let {
            fetchPermissionViaIdQuery(it.roleId)
        } ?: RolePermissions.GUEST_ROLE
        val authorizedRequest = request.with(permissionsLens of permissions)
        next(authorizedRequest)
    }
}
