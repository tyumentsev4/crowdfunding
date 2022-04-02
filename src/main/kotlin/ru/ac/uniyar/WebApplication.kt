package ru.ac.uniyar

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.core.findSingle
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.domain.Project
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.models.NewProjectDataViewModel
import ru.ac.uniyar.models.ProjectViewModel
import ru.ac.uniyar.models.ProjectsListViewModel
import ru.ac.uniyar.models.StartPageViewModel
import java.time.LocalDateTime

fun respondWithPong(): HttpHandler = {
    Response(OK).body("pong")
}

fun showStartPage(renderer: TemplateRenderer): HttpHandler = {
    val viewModel = StartPageViewModel(0)
    Response(OK).body(renderer(viewModel))
}

fun showProjectsList(renderer: TemplateRenderer, projects: Projects): HttpHandler = {
    val viewModel = ProjectsListViewModel(projects.getAllProjects())
    Response(OK).body(renderer(viewModel))
}

fun showNewProjectForm(renderer: TemplateRenderer): HttpHandler = {
    val viewModel = NewProjectDataViewModel(0)
    Response(OK).body(renderer(viewModel))
}

fun createNewProject(projects: Projects): HttpHandler = { request ->
    val params = request.form()
    val name = params.findSingle("name").orEmpty()
    val entrepreneur = params.findSingle("entrepreneur").orEmpty()
    val description = params.findSingle("description").orEmpty()
    val fundSize = params.findSingle("fundSize").orEmpty().toInt()
    val fundraisingStart = LocalDateTime.parse(params.findSingle("fundraisingStart"))

    val fundraisingEnd = LocalDateTime.parse(params.findSingle("fundraisingEnd"))

    val project = Project(name, entrepreneur, description, fundSize, fundraisingStart, fundraisingEnd)
    projects.add(project)
    Response(FOUND).header("Location", "/projects")
}

fun showProject(renderer: TemplateRenderer, projects: Projects): HttpHandler = { request ->
    val index = request.path("index").orEmpty().toInt()
    val project = projects.getProject(index)
    val viewModel = ProjectViewModel(project)
    Response(OK).body(renderer(viewModel))
}

fun app(renderer: TemplateRenderer, projects: Projects): HttpHandler = routes(
    "/ping" bind GET to respondWithPong(),
    "/" bind GET to showStartPage(renderer),
    "/projects/" bind GET to showProjectsList(renderer, projects),
    "/projects/new" bind GET to showNewProjectForm(renderer),
    "/projects/new" bind POST to createNewProject(projects),
    "/projects/{index}" bind GET to showProject(renderer, projects),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)

fun main() {
    val projects = Projects()

    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val printingApp: HttpHandler = PrintRequestAndResponse().then(app(renderer, projects))
    val server = printingApp.asServer(Undertow(port = 9000)).start()
    println("Server started on http://localhost:" + server.port())
}
