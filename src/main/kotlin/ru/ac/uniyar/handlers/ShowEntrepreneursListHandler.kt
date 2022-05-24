package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.dateTime
import org.http4k.lens.int
import org.http4k.lens.string
import ru.ac.uniyar.domain.queries.ListEntrepreneursPerPageQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.EntrepreneursListVM
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowEntrepreneursListHandler(
    private val htmlView: ContextAwareViewRender,
    private val listEntrepreneursPerPageQuery: ListEntrepreneursPerPageQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>
) : HttpHandler {
    companion object {
        private val pageLens = Query.int().defaulted("page", 1)
        private val fromLens = Query.dateTime().optional("fromDateTime")
        private val toLens = Query.dateTime().optional("toDateTime")
        private val nameLens = Query.string().optional("nameSearch")
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if(!permissions.showEntrepreneur)
            return Response(Status.UNAUTHORIZED)

        val pageNumber = lensOrDefault(pageLens, request, 1)
        val fromDateTime = lensOrNull(fromLens, request)
        val toDateTime = lensOrNull(toLens, request)
        val nameSearch = lensOrNull(nameLens, request)
        val pagedElements = listEntrepreneursPerPageQuery.invoke(pageNumber, fromDateTime, toDateTime, nameSearch)
        val paginator = Paginator(pagedElements.pageCount, pageNumber, request.uri)
        val model = EntrepreneursListVM(pagedElements.values, paginator, fromDateTime, toDateTime, nameSearch)

        return Response(Status.OK).with(htmlView(request) of model)
    }
}
