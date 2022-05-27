package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.CloseProjectQuery
import ru.ac.uniyar.domain.queries.FetchProjectViaIdQuery
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.CloseProjectVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowCloseProjectFormHandler(
    private val htmlView: ContextAwareViewRender,
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery,
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
    }
    override fun invoke(request: Request): Response {
        val project = fetchProjectViaIdQuery.invoke(projectIdLens(request))
        val user = currentUserLens(request)
        if (project == null || !project.isOpen())
            return Response(Status.BAD_REQUEST)
        if (project.entrepreneurId != user?.id)
            return Response(Status.UNAUTHORIZED)
        return Response(Status.OK).with(htmlView(request) of CloseProjectVM(project))
    }
}

class CloseProjectHandler(
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery,
    private val closeProjectQuery: CloseProjectQuery,
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val projectId = projectIdLens(request)
        val project = fetchProjectViaIdQuery.invoke(projectId)
        val currentUser = currentUserLens(request)
        if (project == null || !project.isOpen())
            return Response(Status.BAD_REQUEST)
        if (project.entrepreneurId != currentUser?.id)
            return Response(Status.UNAUTHORIZED)

        closeProjectQuery(project)

        return Response(Status.FOUND).header("Location", "/projects/")
    }
}
