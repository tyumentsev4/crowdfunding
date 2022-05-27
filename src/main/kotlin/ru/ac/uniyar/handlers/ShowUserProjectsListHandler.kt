package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.int
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.ListUserProjectsPerPageQuery
import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.UserProjectsListVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowUserProjectsListHandler(
    private val htmlView: ContextAwareViewRender,
    private val listUserProjectsPerPageQuery: ListUserProjectsPerPageQuery,
    private val currentUserLens: RequestContextLens<User?>,
) : HttpHandler {
    companion object {
        private val pageLens = Query.int().defaulted("page", 1)
        private val userIdLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val userId = lensOrNull(userIdLens, request) ?: return Response(Status.BAD_REQUEST)
        val user = currentUserLens(request)
        if ((user == null) || (userId != user.id) || (user.roleId != ENTREPRENEUR_ROLE_ID))
            return Response(Status.UNAUTHORIZED)

        val pageNumber = lensOrDefault(pageLens, request, 1)
        val pagedResult = listUserProjectsPerPageQuery.invoke(pageNumber, userId)
        val paginator = Paginator(pagedResult.pageCount, pageNumber, request.uri)
        val model = UserProjectsListVM(pagedResult.values, paginator)

        return Response(Status.OK).with(htmlView(request) of model)
    }
}
