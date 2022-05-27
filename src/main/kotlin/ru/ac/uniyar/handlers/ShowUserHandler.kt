package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.FetchUserQuery
import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.UserVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowUserHandler(
    private val htmlView: ContextAwareViewRender,
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchUserQuery: FetchUserQuery
) : HttpHandler {
    companion object {
        private val userIdLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val userId = lensOrNull(userIdLens, request) ?: return Response(Status.BAD_REQUEST)
        val user = currentUserLens(request)
        if ((user == null) || (userId != user.id))
            return Response(Status.UNAUTHORIZED)
        val userInfo = fetchUserQuery.invoke(userId)
        return Response(Status.OK).with(
            htmlView(request) of UserVM(userInfo.user, user.roleId == ENTREPRENEUR_ROLE_ID)
        )
    }
}
