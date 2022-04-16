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
import ru.ac.uniyar.domain.queries.ListInvestmentsPerPageQuery
import ru.ac.uniyar.models.InvestmentsListViewModel
import ru.ac.uniyar.models.Paginator

class ShowInvestmentsListHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listInvestmentsPerPageQuery: ListInvestmentsPerPageQuery
) : HttpHandler {
    companion object {
        val pageLens = Query.int().defaulted("page", 1)
    }

    override fun invoke(request: Request): Response {
        val pageNumber = lensOrDefault(pageLens, request, 1)
        val pagedResult = listInvestmentsPerPageQuery.invoke(pageNumber)
        val paginator = Paginator(pagedResult.pageCount, pageNumber, request.uri)
        val model = InvestmentsListViewModel(pagedResult.values, paginator)

        return Response(Status.OK).with(htmlView of model)
    }
}
