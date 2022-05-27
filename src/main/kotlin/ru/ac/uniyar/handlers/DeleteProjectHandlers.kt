package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.DeleteProjectQuery
import ru.ac.uniyar.domain.queries.FetchProjectViaIdQuery
import ru.ac.uniyar.domain.queries.ListInvestmentsQuery
import ru.ac.uniyar.domain.queries.hasProjectInvestments
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.DeleteProjectVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowDeleteProjectFormHandler(
    private val htmlView: ContextAwareViewRender,
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery,
    private val listInvestmentsQuery: ListInvestmentsQuery
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
    }
    override fun invoke(request: Request): Response {
        val project = fetchProjectViaIdQuery.invoke(projectIdLens(request))
        val user = currentUserLens(request)
        if (project == null)
            return Response(Status.BAD_REQUEST)
        if (project.entrepreneurId != user?.id)
            return Response(Status.UNAUTHORIZED)
        if (hasProjectInvestments(project, listInvestmentsQuery()))
            return Response(Status.BAD_REQUEST)
        return Response(Status.OK).with(htmlView(request) of DeleteProjectVM(project))
    }
}

class DeleteProjectHandler(
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery,
    private val deleteProjectQuery: DeleteProjectQuery,
    private val listInvestmentsQuery: ListInvestmentsQuery
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val projectId = projectIdLens(request)
        val project = fetchProjectViaIdQuery.invoke(projectId)
        val currentUser = currentUserLens(request)
        if (project == null)
            return Response(Status.BAD_REQUEST)
        if (project.entrepreneurId != currentUser?.id)
            return Response(Status.UNAUTHORIZED)

        if (hasProjectInvestments(project, listInvestmentsQuery()))
            return Response(Status.BAD_REQUEST)
        else {
            deleteProjectQuery.invoke(project)
        }
        return Response(Status.FOUND).header("Location", "/projects/")
    }
}
