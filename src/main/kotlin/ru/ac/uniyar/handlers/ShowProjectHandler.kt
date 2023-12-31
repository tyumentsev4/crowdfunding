package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.EntrepreneurFetchError
import ru.ac.uniyar.domain.queries.FetchProjectQuery
import ru.ac.uniyar.domain.queries.ProjectFetchError
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.ProjectVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowProjectHandler(
    private val htmlView: ContextAwareViewRender,
    private val fetchProjectQuery: FetchProjectQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>
) : HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.seeProjectInfo)
            return Response(Status.UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(Status.BAD_REQUEST)
        return try {
            val projectInfo = fetchProjectQuery.invoke(id)
            Response(Status.OK).with(
                htmlView(request) of ProjectVM(projectInfo)
            )
        } catch (_: ProjectFetchError) {
            Response(Status.BAD_REQUEST)
        } catch (_: EntrepreneurFetchError) {
            Response(Status.BAD_REQUEST)
        }
    }
}
