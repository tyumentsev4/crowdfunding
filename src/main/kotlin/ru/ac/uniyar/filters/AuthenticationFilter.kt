package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.queries.FetchUserViaToken
import ru.ac.uniyar.domain.storage.User

fun authenticationFilter(
    currentUser: BiDiLens<Request, User?>,
    fetchUserViaToken: FetchUserViaToken,
    jwtTools: JwtTools
): Filter = Filter { next: HttpHandler ->
    { request: Request ->
        val requestWithWorker = request.cookie("token")?.value?.let { token ->
            jwtTools.subject(token)
        }?.let { userId ->
            fetchUserViaToken(userId)
        }.let { user ->
            request.with(currentUser of user)
        }
        next(requestWithWorker)
    }
}
