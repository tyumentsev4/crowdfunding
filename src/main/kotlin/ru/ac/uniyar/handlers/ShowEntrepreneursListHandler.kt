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
import ru.ac.uniyar.domain.queries.ListEntrepreneursPerPageQuery
import ru.ac.uniyar.models.EntrepreneursListViewModel
import ru.ac.uniyar.models.Paginator

class ShowEntrepreneursListHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listEntrepreneursPerPageQuery: ListEntrepreneursPerPageQuery
) : HttpHandler {
    companion object {
        private val pageLens = Query.int().defaulted("page", 1)
    }

    override fun invoke(request: Request): Response {
        val pageNumber = lensOrDefault(pageLens, request, 1)
        val pagedElements = listEntrepreneursPerPageQuery.invoke(pageNumber)
        val paginator = Paginator(pagedElements.pageCount, pageNumber, request.uri)
        val model = EntrepreneursListViewModel(pagedElements.values, paginator)

        return Response(Status.OK).with(htmlView of model)
    }
}
