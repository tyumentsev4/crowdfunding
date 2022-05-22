package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.BiDiLens

fun authenticationFilter(
    currentUser: BiDiLens<Request, User?>,
    fetchUserViaUserId: FetchUserViaUserId,
    jwtTools: JwtTools
): Filter = Filter { next: HttpHandler ->
    { request: Request ->
        val requestWithWorker = request.cookie("token")?.value?.let { token ->
            jwtTools.subject(token)
        }?.let { userId ->
            fetchUserViaUserId(userId)
        }.let { user ->
            request.with(currentUser of user)
        }
        next(requestWithWorker)
    }
}
