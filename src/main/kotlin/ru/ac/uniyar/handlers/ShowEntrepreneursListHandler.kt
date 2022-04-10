package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.removeQuery
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.LensFailure
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Store
import ru.ac.uniyar.models.EntrepreneursListViewModel
import ru.ac.uniyar.models.Paginator

fun showEntrepreneursList(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = handler@{ request ->
    val pageLens = Query.int().defaulted("page", 1)
    val safeReturnResponse = Response(Status.FOUND).header(
        "Location", request.uri.removeQuery("page").toString()
    )
    val pageNumber = try {
        pageLens(request)
    } catch (failure: LensFailure) {
        return@handler safeReturnResponse.header("Error", failure.message)
    }
    val repository = store.entrepreneursRepository
    val elements = repository.listEntrepreneurs(pageNumber)
    val paginator = Paginator(elements.pageCount, pageNumber, request.uri)
    val model = EntrepreneursListViewModel(elements.values, paginator)

    Response(Status.OK).with(htmlView of model)
}
