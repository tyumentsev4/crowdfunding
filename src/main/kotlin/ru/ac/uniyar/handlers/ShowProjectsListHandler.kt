package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.boolean
import org.http4k.lens.int
import ru.ac.uniyar.domain.queries.ListProjectsPerPageQuery
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.ProjectsListViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowProjectsListHandler(
    private val htmlView: ContextAwareViewRender,
    private val listProjectsPerPageQuery: ListProjectsPerPageQuery
) : HttpHandler {
    companion object {
        private val pageLens = Query.int().defaulted("page", 1)
        private val fromLens = Query.int().optional("fromFoundSize")
        private val toLens = Query.int().optional("toFoundSize")
        private val isOpenLens = Query.boolean().optional("isOpen")
    }

    override fun invoke(request: Request): Response {
        val pageNumber = lensOrDefault(pageLens, request, 1)
        val fromFoundSize = lensOrNull(fromLens, request)
        val toFoundSize = lensOrNull(toLens, request)
        val isOpen = lensOrNull(isOpenLens, request)
        val pagedResult = listProjectsPerPageQuery.invoke(pageNumber, fromFoundSize, toFoundSize, isOpen)
        val paginator = Paginator(pagedResult.pageCount, pageNumber, request.uri)
        val model = ProjectsListViewModel(pagedResult.values, paginator, fromFoundSize, toFoundSize, isOpen)

        return Response(Status.OK).with(htmlView(request) of model)
    }
}
