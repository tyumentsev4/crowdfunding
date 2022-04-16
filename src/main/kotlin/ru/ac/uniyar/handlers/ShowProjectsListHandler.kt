package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.ListProjectsPerPageQuery
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.ProjectsListViewModel

class ShowProjectsListHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listProjectsPerPageQuery: ListProjectsPerPageQuery
) : HttpHandler {
    companion object {
        val pageLens = Query.int().defaulted("page", 1)
    }

    override fun invoke(request: Request): Response {
        val pageNumber = lensOrDefault(pageLens, request, 1)
        val pagedResult = listProjectsPerPageQuery.invoke(pageNumber)
        val paginator = Paginator(pagedResult.pageCount, pageNumber, request.uri)
        val model = ProjectsListViewModel(pagedResult.values, paginator)

        return Response(Status.OK).with(htmlView of model)
    }
}
