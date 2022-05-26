package ru.ac.uniyar.handlers

import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.StoreInitializer
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.filters.JwtTools
import ru.ac.uniyar.models.template.ContextAwareViewRender

class HttpHandlerInitializer (
    currentUserLens: RequestContextLens<User?>,
    permissionsLens: RequestContextLens<RolePermissions>,
    htmlView: ContextAwareViewRender,
    storeInitializer: StoreInitializer,
    jwtTools: JwtTools
) {
    val logOutUser = LogOutUser()
    val authenticateUser = AuthenticateUser(
        storeInitializer.authenticateUserViaLoginQuery,
        htmlView,
        jwtTools
    )
    val showLoginFormHandler = ShowLoginFormHandler(
        htmlView
    )
    val showNewUserFormHandler = ShowNewUserFormHandler(
        htmlView
    )
    val addUserHandler = AddUserHandler(
        htmlView,
        storeInitializer.addUserQuery
    )
    val showNewInvestmentFormHandler = ShowNewInvestmentFormHandler(
        htmlView,
        storeInitializer.listOpenProjectQuery,
        storeInitializer.fetchProjectViaIdQuery,
        permissionsLens
    )
    val addInvestmentHandler = AddInvestmentHandler(
        htmlView,
        storeInitializer.listOpenProjectQuery,
        storeInitializer.addInvestmentQuery,
        storeInitializer.fetchProjectViaIdQuery,
        currentUserLens,
        permissionsLens
    )
    val showNewProjectFormHandler = ShowNewProjectFormHandler(
        htmlView,
        permissionsLens
    )
    val addProjectHandler = AddProjectHandler(
        htmlView,
        storeInitializer.addProjectQuery,
        permissionsLens,
        currentUserLens
    )
    val showEntrepreneurHandler = ShowEntrepreneurHandler(
        htmlView,
        storeInitializer.fetchEntrepreneurQuery,
        permissionsLens
    )
    val showEntrepreneursListHandler = ShowEntrepreneursListHandler(
        htmlView,
        storeInitializer.listEntrepreneursPerPageQuery,
        permissionsLens
    )
    val showInvestmentHandler = ShowInvestmentHandler(
        htmlView,
        storeInitializer.fetchInvestmentQuery,
        permissionsLens
    )
    val showInvestmentsListHandler = ShowInvestmentsListHandler(
        htmlView,
        storeInitializer.listInvestmentsPerPageQuery
    )
    val showProjectHandler = ShowProjectHandler(
        htmlView,
        storeInitializer.fetchProjectQuery,
        permissionsLens
    )
    val showProjectsListHandler = ShowProjectsListHandler(
        htmlView,
        storeInitializer.listProjectsPerPageQuery,
        permissionsLens
    )
    val showStartPageHandler = ShowStartPageHandler(
        htmlView
    )
    val showUserHandler = ShowUserHandler(
        htmlView,
        currentUserLens,
        storeInitializer.fetchUserQuery
    )
    val showUserProjectsListHandler = ShowUserProjectsListHandler(
        htmlView,
        storeInitializer.listUserProjectsPerPageQuery,
        currentUserLens
    )
    val showEditProjectHandler = ShowEditProjectFormHandler(
        htmlView,
        currentUserLens,
        storeInitializer.fetchProjectViaIdQuery
    )
    val editProjectHandler = EditProjectHandler(
        htmlView,
        storeInitializer.editProjectQuery,
        currentUserLens,
        storeInitializer.fetchProjectViaIdQuery
    )
    val showDeleteProjectFormHandler = ShowDeleteProjectFormHandler(
        htmlView,
        currentUserLens,
        storeInitializer.fetchProjectViaIdQuery,
        storeInitializer.investmentsByProjectQuery
    )
    val deleteProjectHandler = DeleteProjectHandler(
        currentUserLens,
        storeInitializer.fetchProjectViaIdQuery,
        storeInitializer.deleteProjectQuery,
        storeInitializer.investmentsByProjectQuery
    )
}