package ru.ac.uniyar

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

@Suppress("LongParameterList")
class Router(
    private val showNewEntrepreneurFormHandler: HttpHandler,
    private val addEntrepreneurHandler: HttpHandler,
    private val showNewInvestmentFormHandler: HttpHandler,
    private val addInvestmentHandler: HttpHandler,
    private val showNewProjectFormHandler: HttpHandler,
    private val addProjectHandler: HttpHandler,
    private val showEntrepreneurHandler: HttpHandler,
    private val showEntrepreneursListHandler: HttpHandler,
    private val showInvestmentHandler: HttpHandler,
    private val showInvestmentsListHandler: HttpHandler,
    private val showProjectHandler: HttpHandler,
    private val showProjectsListHandler: HttpHandler,
    private val showStartPageHandler: HttpHandler,
    private val staticFilesHandler: RoutingHttpHandler
) {
    operator fun invoke(): RoutingHttpHandler = routes(
        "/" bind Method.GET to showStartPageHandler,
        "/entrepreneurs" bind Method.GET to showEntrepreneursListHandler,
        "/entrepreneurs/new" bind Method.GET to showNewEntrepreneurFormHandler,
        "/entrepreneurs/new" bind Method.POST to addEntrepreneurHandler,
        "/entrepreneurs/{id}" bind Method.GET to showEntrepreneurHandler,
        "/projects/" bind Method.GET to showProjectsListHandler,
        "/projects/new" bind Method.GET to showNewProjectFormHandler,
        "/projects/new" bind Method.POST to addProjectHandler,
        "/projects/{id}" bind Method.GET to showProjectHandler,
        "/investments" bind Method.GET to showInvestmentsListHandler,
        "/investments/new" bind Method.GET to showNewInvestmentFormHandler,
        "/investments/new" bind Method.POST to addInvestmentHandler,
        "/investments/{id}" bind Method.GET to showInvestmentHandler,
        staticFilesHandler,
    )
}
