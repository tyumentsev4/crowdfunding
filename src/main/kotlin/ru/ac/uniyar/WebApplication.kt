package ru.ac.uniyar

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.lens.BiDiBodyLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.viewModel
import ru.ac.uniyar.domain.Entrepreneurs
import ru.ac.uniyar.domain.Investments
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.filters.showErrorMessageFilter
import ru.ac.uniyar.handlers.respondWithPong
import ru.ac.uniyar.handlers.showEntrepreneursList
import ru.ac.uniyar.handlers.showInvestmentsList
import ru.ac.uniyar.handlers.showProject
import ru.ac.uniyar.handlers.showProjectsList
import ru.ac.uniyar.handlers.showStartPage
import ru.ac.uniyar.routes.entrepreneurCreationRoute
import ru.ac.uniyar.routes.investmentsCreationRoute
import ru.ac.uniyar.routes.projectCreationRoute

fun app(
    renderer: TemplateRenderer,
    projects: Projects,
    htmlView: BiDiBodyLens<ViewModel>,
    entrepreneurs: Entrepreneurs,
    investments: Investments
): HttpHandler = routes(
    "/ping" bind GET to respondWithPong(),
    "/" bind GET to showStartPage(renderer),
    "/projects/" bind GET to showProjectsList(renderer, projects),
    "/entrepreneurs" bind GET to showEntrepreneursList(renderer, entrepreneurs),
    "/investments" bind GET to showInvestmentsList(renderer, investments),
    "/projects/new" bind projectCreationRoute(projects, htmlView, entrepreneurs),
    "/entrepreneurs/new" bind entrepreneurCreationRoute(entrepreneurs, htmlView),
    "/investments/new" bind investmentsCreationRoute(investments, projects, htmlView),
    "/projects/{index}" bind GET to showProject(renderer, projects),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)

fun main() {
    val projects = Projects()
    val entrepreneurs = Entrepreneurs()
    val investments = Investments()

    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val htmlView = Body.viewModel(renderer, ContentType.TEXT_HTML).toLens()
    val printingApp: HttpHandler = PrintRequestAndResponse()
        .then(showErrorMessageFilter(renderer))
        .then(app(renderer, projects, htmlView, entrepreneurs, investments))
    val server = printingApp.asServer(Undertow(port = 9000)).start()
    println("Server started on http://localhost:" + server.port())
}
