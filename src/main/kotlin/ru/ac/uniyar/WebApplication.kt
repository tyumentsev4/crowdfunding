package ru.ac.uniyar

import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.routing.*
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.domain.Project
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.models.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


fun respondWithPong(): HttpHandler = {
    Response(OK).body("pong")
}

fun showTemplate(renderer: TemplateRenderer): HttpHandler = {
    val viewModel = PebbleViewModel("Hello there!")
    Response(OK).body(renderer(viewModel))
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

fun createNewProject(renderer: TemplateRenderer, projects: Projects): HttpHandler = {request ->
    val params = request.form()
    val name = params.findSingle("name").orEmpty()
    val entrepreneur = params.findSingle("entrepreneur").orEmpty()
    val description = params.findSingle("description").orEmpty()
    val fundSize = params.findSingle("fundSize").orEmpty().toInt()
    val fundraisingStart = LocalDateTime.of(
        LocalDate.parse(params.findSingle("fundraisingStartDate")),
        LocalTime.parse(params.findSingle("fundraisingStartTime"))
    )
    val fundraisingEnd = LocalDateTime.of(
        LocalDate.parse(params.findSingle("fundraisingEndDate")),
        LocalTime.parse(params.findSingle("fundraisingEndTime"))
    )

    val project = Project(name, entrepreneur, description, fundSize, fundraisingStart, fundraisingEnd)
    projects.add(project)
    Response(FOUND).header("Location", "/projects")
}

fun showProject(renderer: TemplateRenderer, projects: Projects): HttpHandler = { request ->
    val number = request.path("number").orEmpty().toInt()
    val project = projects.getProject(number-1)
    val viewModel = ProjectViewModel(project)
    Response(OK).body(renderer(viewModel))
}

fun app(renderer: TemplateRenderer, projects: Projects): HttpHandler = routes(
    "/ping" bind GET to respondWithPong(),
    "/templates/pebble" bind GET to showTemplate(renderer),
    "/" bind GET to showStartPage(renderer),
    "/projects/" bind GET to showProjectsList(renderer, projects),
    "/projects/new" bind GET to showNewProjectForm(renderer),
    "/projects/new" bind Method.POST to createNewProject(renderer, projects),
    "/projects/{number}" bind GET to showProject(renderer, projects),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)

fun main() {
    val projects = Projects()

    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val printingApp: HttpHandler = PrintRequestAndResponse().then(app(renderer, projects))
    val server = printingApp.asServer(Undertow(9000)).start()
    println("Server started on http://localhost:" + server.port())
}
