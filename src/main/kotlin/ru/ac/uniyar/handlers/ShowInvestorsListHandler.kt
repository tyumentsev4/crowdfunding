package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.FetchUserProjectsInvestorsQuery
import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.ProjectsInvestorsListVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowInvestorsListHandler(
    private val htmlView: ContextAwareViewRender,
    private val fetchUserProjectsInvestorsQuery: FetchUserProjectsInvestorsQuery,
    private val currentUserLens: RequestContextLens<User?>,
) : HttpHandler {
    companion object {
        private val userIdLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val userId = lensOrNull(userIdLens, request) ?: return Response(Status.BAD_REQUEST)
        val user = currentUserLens(request)
        if ((user == null) || (userId != user.id) || (user.roleId != ENTREPRENEUR_ROLE_ID))
            return Response(Status.UNAUTHORIZED)

        val model = ProjectsInvestorsListVM(fetchUserProjectsInvestorsQuery(userId))

        return Response(Status.OK).with(htmlView(request) of model)
    }
}
