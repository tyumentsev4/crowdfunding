package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.EntrepreneurFetchError
import ru.ac.uniyar.domain.queries.FetchProjectQuery
import ru.ac.uniyar.domain.queries.ProjectFetchError
import ru.ac.uniyar.models.ProjectViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowProjectHandler(
    private val htmlView: ContextAwareViewRender,
    private val fetchProjectQuery: FetchProjectQuery
) : HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val id = lensOrNull(idLens, request) ?: return Response(Status.BAD_REQUEST)
        return try {
            val projectInfo = fetchProjectQuery.invoke(id)
            Response(Status.OK).with(
                htmlView(request) of ProjectViewModel(projectInfo)
            )
        } catch (_: ProjectFetchError) {
            Response(Status.BAD_REQUEST)
        } catch (_: EntrepreneurFetchError) {
            Response(Status.BAD_REQUEST)
        }
    }
}
