package ru.ac.uniyar.handlers

import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.StoreHolder
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.filters.JwtTools
import ru.ac.uniyar.models.template.ContextAwareViewRender

class HttpHandlerHolder (
    currentUserLens: RequestContextLens<User?>,
    htmlView: ContextAwareViewRender,
    storeHolder: StoreHolder,
    jwtTools: JwtTools
) {
    val showNewEntrepreneurFormHandler = ShowNewEntrepreneurFormHandler(
        htmlView
    )
    val addEntrepreneurHandler = AddEntrepreneurHandler(
        htmlView,
        storeHolder.addEntrepreneurQuery
    )
    val showNewInvestmentFormHandler = ShowNewInvestmentFormHandler(
        htmlView,
        storeHolder.listOpenProjectQuery
    )
    val addInvestmentHandler = AddInvestmentHandler(
        htmlView,
        storeHolder.listOpenProjectQuery,
        storeHolder.addInvestmentQuery
    )
    val showNewProjectFormHandler = ShowNewProjectFormHandler(
        htmlView,
        storeHolder.listEntrepreneursQuery
    )
    val addProjectHandler = AddProjectHandler(
        htmlView,
        storeHolder.listEntrepreneursQuery,
        storeHolder.addProjectQuery
    )
    val showEntrepreneurHandler = ShowEntrepreneurHandler(
        htmlView,
        storeHolder.fetchEntrepreneurQuery
    )
    val showEntrepreneursListHandler = ShowEntrepreneursListHandler(
        htmlView,
        storeHolder.listEntrepreneursPerPageQuery
    )
    val showInvestmentHandler = ShowInvestmentHandler(
        htmlView,
        storeHolder.fetchInvestmentQuery
    )
    val showInvestmentsListHandler = ShowInvestmentsListHandler(
        htmlView,
        storeHolder.listInvestmentsPerPageQuery
    )
    val showProjectHandler = ShowProjectHandler(
        htmlView,
        storeHolder.fetchProjectQuery
    )
    val showProjectsListHandler = ShowProjectsListHandler(
        htmlView,
        storeHolder.listProjectsPerPageQuery
    )
    val showStartPageHandler = ShowStartPageHandler(
        htmlView
    )
}