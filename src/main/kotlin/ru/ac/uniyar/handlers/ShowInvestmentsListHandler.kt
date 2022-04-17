package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.Query
import org.http4k.lens.dateTime
import org.http4k.lens.double
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
        private val fromLens = Query.dateTime().optional("fromDateTime")
        private val toLens = Query.dateTime().optional("toDateTime")
        private val fromAmount = Query.double().optional("fromAmount")
        private val toAmount = Query.double().optional("toAmount")
    }

    override fun invoke(request: Request): Response {
        val pageNumber = lensOrDefault(pageLens, request, 1)
        val fromDateTime = lensOrNull(fromLens, request)
        val toDateTime = lensOrNull(toLens, request)
        val fromAmount = lensOrNull(fromAmount, request)
        val toAmount = lensOrNull(toAmount, request)
        val pagedResult = listInvestmentsPerPageQuery.invoke(pageNumber, fromDateTime, toDateTime, fromAmount, toAmount)
        val paginator = Paginator(pagedResult.pageCount, pageNumber, request.uri)
        val model =
            InvestmentsListViewModel(pagedResult.values, paginator, fromDateTime, toDateTime, fromAmount, toAmount)

        return Response(Status.OK).with(htmlView of model)
    }
}
