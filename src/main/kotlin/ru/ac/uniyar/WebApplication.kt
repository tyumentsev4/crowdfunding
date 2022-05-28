package ru.ac.uniyar

import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.server.Undertow
import org.http4k.server.asServer
import ru.ac.uniyar.domain.StoreInitializer
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.SettingFileError
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.filters.JwtTools
import ru.ac.uniyar.filters.authenticationFilter
import ru.ac.uniyar.filters.authorizationFilter
import ru.ac.uniyar.filters.showErrorMessageFilter
import ru.ac.uniyar.handlers.HttpHandlerInitializer
import ru.ac.uniyar.models.template.ContextAwarePebbleTemplates
import ru.ac.uniyar.models.template.ContextAwareViewRender

const val SERVER_PORT = 9000

fun main() {
    val storeInitializer = try {
        StoreInitializer(java.nio.file.Path.of("storage.json"), java.nio.file.Path.of("settings.json"))
    } catch (error: SettingFileError) {
        println(error.message)
        return
    }

    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender(renderer, ContentType.TEXT_HTML)

    val contexts = RequestContexts()
    val currentUserLens: RequestContextLens<User?> = RequestContextKey.optional(contexts, "user")
    val permissionsLens: RequestContextLens<RolePermissions> =
        RequestContextKey.required(contexts, name = "permissions")
    val htmlViewWithContext = htmlView
        .associateContextLens("currentUser", currentUserLens)
        .associateContextLens("permissions", permissionsLens)
    val jwtTools = JwtTools(storeInitializer.settings.salt, "ru.ac.uniyar.WebApplication")
    val handlerHolder = HttpHandlerInitializer(
        currentUserLens,
        permissionsLens,
        htmlViewWithContext,
        storeInitializer,
        jwtTools
    )

    val router = Router(
        handlerHolder.showNewUserFormHandler,
        handlerHolder.addUserHandler,
        handlerHolder.showNewInvestmentFormHandler,
        handlerHolder.addInvestmentHandler,
        handlerHolder.showNewProjectFormHandler,
        handlerHolder.addProjectHandler,
        handlerHolder.showEntrepreneurHandler,
        handlerHolder.showEntrepreneursListHandler,
        handlerHolder.showInvestmentHandler,
        handlerHolder.showProjectHandler,
        handlerHolder.showProjectsListHandler,
        handlerHolder.showStartPageHandler,
        handlerHolder.showLoginFormHandler,
        handlerHolder.authenticateUser,
        handlerHolder.logOutUser,
        handlerHolder.showUserHandler,
        handlerHolder.showUserProjectsListHandler,
        handlerHolder.showEditProjectHandler,
        handlerHolder.editProjectHandler,
        handlerHolder.showDeleteProjectFormHandler,
        handlerHolder.deleteProjectHandler,
        handlerHolder.showCloseProjectFormHandler,
        handlerHolder.closeProjectHandler,
        handlerHolder.showInvestorsListHandler,
    )

    val printingApp: HttpHandler =
        ServerFilters.InitialiseRequestContext(contexts)
            .then(
                authenticationFilter(
                    currentUserLens,
                    storeInitializer.fetchUserViaToken,
                    jwtTools
                ).then(
                    authorizationFilter(
                        currentUserLens,
                        permissionsLens,
                        storeInitializer.fetchPermissionsViaIdQuery
                    )
                ).then(
                    showErrorMessageFilter(htmlViewWithContext)
                ).then(router())
            )

    val server = printingApp.asServer(Undertow(SERVER_PORT)).start()
    println("Server started on http://localhost:" + server.port())
}
