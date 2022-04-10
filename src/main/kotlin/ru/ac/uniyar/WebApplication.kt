package ru.ac.uniyar

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.NoOp
import org.http4k.core.then
import org.http4k.lens.BiDiBodyLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel
import ru.ac.uniyar.domain.Store
import ru.ac.uniyar.filters.showErrorMessageFilter
import ru.ac.uniyar.handlers.respondWithPong
import ru.ac.uniyar.handlers.showEntrepreneur
import ru.ac.uniyar.handlers.showEntrepreneursList
import ru.ac.uniyar.handlers.showInvestment
import ru.ac.uniyar.handlers.showInvestmentsList
import ru.ac.uniyar.handlers.showProject
import ru.ac.uniyar.handlers.showProjectsList
import ru.ac.uniyar.handlers.showStartPage
import ru.ac.uniyar.routes.entrepreneurCreationRoute
import ru.ac.uniyar.routes.investmentsCreationRoute
import ru.ac.uniyar.routes.projectCreationRoute
import kotlin.io.path.Path

fun app(
    htmlView: BiDiBodyLens<ViewModel>,
    store: Store
): HttpHandler = routes(
    "/ping" bind GET to respondWithPong(),
    "/" bind GET to showStartPage(htmlView),
    "/projects/" bind GET to showProjectsList(htmlView, store),
    "/entrepreneurs" bind GET to showEntrepreneursList(htmlView, store),
    "/investments" bind GET to showInvestmentsList(htmlView, store),
    "/projects/new" bind projectCreationRoute(htmlView, store),
    "/entrepreneurs/new" bind entrepreneurCreationRoute(htmlView, store),
    "/investments/new" bind investmentsCreationRoute(htmlView, store),
    "/projects/{id}" bind GET to showProject(htmlView, store),
    "/entrepreneurs/{id}" bind GET to showEntrepreneur(htmlView, store),
    "/investments/{id}" bind GET to showInvestment(htmlView, store),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)

const val SERVER_PORT = 9000

fun main() {
    val store = Store(Path("storage.json"))
    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val htmlView = Body.viewModel(renderer, ContentType.TEXT_HTML).toLens()
    val application = app(htmlView, store)
    val printingApp: HttpHandler =
        Filter.NoOp
            .then(showErrorMessageFilter(renderer))
            .then(application)
    val server = printingApp.asServer(Undertow(SERVER_PORT)).start()
    println("Server started on http://localhost:" + server.port())
}
