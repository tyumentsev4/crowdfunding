package ru.ac.uniyar

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.NoOp
import org.http4k.core.then
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.viewModel
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
import ru.ac.uniyar.domain.storage.Store
import ru.ac.uniyar.filters.showErrorMessageFilter
import kotlin.io.path.Path

const val SERVER_PORT = 9000

fun main() {
    val store = Store(Path("storage.json"))
    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val htmlView = Body.viewModel(renderer, ContentType.TEXT_HTML).toLens()

    val listProjectsPerPageQuery = ListProjectsPerPageQuery(store)
    val listEntrepreneursPerPageQuery = ListEntrepreneursPerPageQuery(store)
    val fetchEntrepreneurQuery = FetchEntrepreneurQuery(store)
    val addEntrepreneurQuery = AddEntrepreneurQuery(store)
    val fetchProjectQuery = FetchProjectQuery(store)
    val listEntrepreneursQuery = ListEntrepreneursQuery(store)
    val addProjectQuery = AddProjectQuery(store)
    val listInvestmentsPerPageQuery = ListInvestmentsPerPageQuery(store)
    val listProjectQuery = ListOpenProjectsQuery(store)
    val addInvestmentQuery = AddInvestmentQuery(store)
    val fetchInvestmentQuery = FetchInvestmentQuery(store)

    val router = Router(
        htmlView,
        listProjectsPerPageQuery,
        listEntrepreneursPerPageQuery,
        fetchEntrepreneurQuery,
        addEntrepreneurQuery,
        fetchProjectQuery,
        listEntrepreneursQuery,
        addProjectQuery,
        listInvestmentsPerPageQuery,
        listProjectQuery,
        addInvestmentQuery,
        fetchInvestmentQuery
    )

    val printingApp: HttpHandler =
        Filter.NoOp
            .then(showErrorMessageFilter(renderer))
            .then(router())
    val server = printingApp.asServer(Undertow(SERVER_PORT)).start()
    println("Server started on http://localhost:" + server.port())
}
