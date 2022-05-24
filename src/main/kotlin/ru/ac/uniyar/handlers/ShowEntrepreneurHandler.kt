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
import ru.ac.uniyar.domain.queries.FetchEntrepreneurQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.EntrepreneurVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowEntrepreneurHandler(
    private val htmlView: ContextAwareViewRender,
    private val fetchEntrepreneurQuery: FetchEntrepreneurQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>
) : HttpHandler {

    companion object {
        private val idLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.showEntrepreneur)
            return Response(Status.UNAUTHORIZED)

        val id = lensOrNull(idLens, request) ?: return Response(Status.BAD_REQUEST)
        return try {
            val entrepreneurInfo = fetchEntrepreneurQuery.invoke(id)
            Response(Status.OK).with(
                htmlView(request) of EntrepreneurVM(entrepreneurInfo.entrepreneur, entrepreneurInfo.projects)
            )
        } catch (_: EntrepreneurFetchError) {
            Response(Status.BAD_REQUEST)
        }
    }
}
