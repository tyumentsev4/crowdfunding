package ru.ac.uniyar

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

@Suppress("LongParameterList")
class Router(
    private val showNewUserFormHandler: HttpHandler,
    private val addUserHandler: HttpHandler,
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
    private val showLoginFormHandler: HttpHandler,
    private val authenticateUser: HttpHandler,
    private val logOutUser: HttpHandler,
) {
    operator fun invoke(): RoutingHttpHandler = routes(
        "/" bind Method.GET to showStartPageHandler,
        "/login" bind Method.GET to showLoginFormHandler,
        "/login" bind Method.POST to authenticateUser,
        "/logout" bind Method.GET to logOutUser,
        "/registration" bind Method.GET to showNewUserFormHandler,
        "/registration" bind Method.POST to addUserHandler,
        "/entrepreneurs" bind Method.GET to showEntrepreneursListHandler,
        "/entrepreneurs/{id}" bind Method.GET to showEntrepreneurHandler,
        "/projects/" bind Method.GET to showProjectsListHandler,
        "/projects/new" bind Method.GET to showNewProjectFormHandler,
        "/projects/new" bind Method.POST to addProjectHandler,
        "/projects/{id}" bind Method.GET to showProjectHandler,
        "/investments" bind Method.GET to showInvestmentsListHandler,
        "/projects/{id}/new_investment" bind Method.GET to showNewInvestmentFormHandler,
        "/projects/{id}/new_investment" bind Method.POST to addInvestmentHandler,
        "/investments/{id}" bind Method.GET to showInvestmentHandler,
    )
}
