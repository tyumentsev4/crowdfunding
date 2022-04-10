package ru.ac.uniyar

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.lens.BiDiBodyLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.path
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
import ru.ac.uniyar.models.EntrepreneursListViewModel
import ru.ac.uniyar.models.InvestmentsListViewModel
import ru.ac.uniyar.models.ProjectViewModel
import ru.ac.uniyar.models.ProjectsListViewModel
import ru.ac.uniyar.models.ShowErrorInfoViewModel
import ru.ac.uniyar.models.StartPageViewModel
import ru.ac.uniyar.routes.entrepreneurCreationRoute
import ru.ac.uniyar.routes.investmentsCreationRoute
import ru.ac.uniyar.routes.projectCreationRoute
import kotlin.concurrent.thread

fun respondWithPong(): HttpHandler = {
    Response(OK).body("pong")
}

fun showStartPage(renderer: TemplateRenderer): HttpHandler = {
    val viewModel = StartPageViewModel(0)
    Response(OK).body(renderer(viewModel))
}

fun showProjectsList(renderer: TemplateRenderer, projects: Projects): HttpHandler = {
    val viewModel = ProjectsListViewModel(projects.getAll())
    Response(OK).body(renderer(viewModel))
}

fun showEntrepreneursList(renderer: TemplateRenderer, entrepreneurs: Entrepreneurs): HttpHandler = {
    Response(OK).body(renderer(EntrepreneursListViewModel(entrepreneurs.getAll())))
}

fun showInvestmentsList(renderer: TemplateRenderer, investments: Investments): HttpHandler = {
    Response(OK).body(renderer(InvestmentsListViewModel(investments.getAll())))
}

fun showProject(renderer: TemplateRenderer, projects: Projects): HttpHandler = handler@{ request ->
    val indexString = request.path("index").orEmpty()
    val index = indexString.toIntOrNull() ?: return@handler Response(BAD_REQUEST)
    val project = projects.getProject(index) ?: return@handler Response(BAD_REQUEST)
    val viewModel = ProjectViewModel(project)
    Response(OK).body(renderer(viewModel))
}

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

fun showErrorMessageFilter(renderer: TemplateRenderer): Filter = Filter { next: HttpHandler ->
    { request ->
        val response = next(request)
        if (response.status.successful) {
            response
        } else {
            response.body(renderer(ShowErrorInfoViewModel(request.uri)))
        }
    }
}

fun main() {
    val storeThread = thread(start = false, name = "Store file save") {
        println("!!!!")
//        save()
    }
    Runtime.getRuntime().addShutdownHook(storeThread)


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
