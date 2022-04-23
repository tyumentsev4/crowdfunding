package ru.ac.uniyar

import org.http4k.core.Method
import org.http4k.lens.BiDiBodyLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.AddEntrepreneurQuery
import ru.ac.uniyar.domain.queries.AddInvestmentQuery
import ru.ac.uniyar.domain.queries.AddProjectQuery
import ru.ac.uniyar.domain.queries.FetchEntrepreneurQuery
import ru.ac.uniyar.domain.queries.FetchInvestmentQuery
import ru.ac.uniyar.domain.queries.FetchProjectQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursPerPageQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursQuery
import ru.ac.uniyar.domain.queries.ListInvestmentsPerPageQuery
import ru.ac.uniyar.domain.queries.ListOpenProjectsQuery
import ru.ac.uniyar.domain.queries.ListProjectsPerPageQuery
import ru.ac.uniyar.handlers.AddEntrepreneurHandler
import ru.ac.uniyar.handlers.AddInvestmentHandler
import ru.ac.uniyar.handlers.AddProjectHandler
import ru.ac.uniyar.handlers.ShowEntrepreneurHandler
import ru.ac.uniyar.handlers.ShowEntrepreneursListHandler
import ru.ac.uniyar.handlers.ShowInvestmentHandler
import ru.ac.uniyar.handlers.ShowInvestmentsListHandler
import ru.ac.uniyar.handlers.ShowNewEntrepreneurFormHandler
import ru.ac.uniyar.handlers.ShowNewInvestmentFormHandler
import ru.ac.uniyar.handlers.ShowNewProjectFormHandler
import ru.ac.uniyar.handlers.ShowProjectHandler
import ru.ac.uniyar.handlers.ShowProjectsListHandler
import ru.ac.uniyar.handlers.ShowStartPageHandler

@Suppress("LongParameterList")
class Router(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listProjectsPerPageQuery: ListProjectsPerPageQuery,
    private val listEntrepreneursPerPageQuery: ListEntrepreneursPerPageQuery,
    private val fetchEntrepreneurQuery: FetchEntrepreneurQuery,
    private val addEntrepreneurQuery: AddEntrepreneurQuery,
    private val fetchProjectQuery: FetchProjectQuery,
    private val listEntrepreneursQuery: ListEntrepreneursQuery,
    private val addProjectQuery: AddProjectQuery,
    private val listInvestmentsPerPageQuery: ListInvestmentsPerPageQuery,
    private val listOpenProjectsQuery: ListOpenProjectsQuery,
    private val addInvestmentQuery: AddInvestmentQuery,
    private val fetchInvestmentQuery: FetchInvestmentQuery

) {
    operator fun invoke(): RoutingHttpHandler = routes(
        "/" bind Method.GET to ShowStartPageHandler(htmlView),
        "/entrepreneurs" bind Method.GET to ShowEntrepreneursListHandler(htmlView, listEntrepreneursPerPageQuery),
        "/entrepreneurs/new" bind Method.GET to ShowNewEntrepreneurFormHandler(htmlView),
        "/entrepreneurs/new" bind Method.POST to AddEntrepreneurHandler(htmlView, addEntrepreneurQuery),
        "/entrepreneurs/{id}" bind Method.GET to ShowEntrepreneurHandler(htmlView, fetchEntrepreneurQuery),
        "/projects/" bind Method.GET to ShowProjectsListHandler(htmlView, listProjectsPerPageQuery),
        "/projects/new" bind Method.GET to ShowNewProjectFormHandler(htmlView, listEntrepreneursQuery),
        "/projects/new" bind Method.POST to AddProjectHandler(htmlView, listEntrepreneursQuery, addProjectQuery),
        "/projects/{id}" bind Method.GET to ShowProjectHandler(htmlView, fetchProjectQuery),
        "/investments" bind Method.GET to ShowInvestmentsListHandler(htmlView, listInvestmentsPerPageQuery),
        "/investments/new" bind Method.GET to ShowNewInvestmentFormHandler(htmlView, listOpenProjectsQuery),
        "/investments/new" bind Method.POST to AddInvestmentHandler(
            htmlView,
            listOpenProjectsQuery,
            addInvestmentQuery
        ),
        "/investments/{id}" bind Method.GET to ShowInvestmentHandler(htmlView, fetchInvestmentQuery),
        static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
    )
}
