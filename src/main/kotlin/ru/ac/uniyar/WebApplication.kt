package ru.ac.uniyar

import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.static
import org.http4k.server.Undertow
import org.http4k.server.asServer
import ru.ac.uniyar.domain.StoreHolder
import ru.ac.uniyar.domain.storage.SettingFileError
import ru.ac.uniyar.filters.JwtTools
import ru.ac.uniyar.filters.showErrorMessageFilter
import ru.ac.uniyar.handlers.HttpHandlerHolder
import ru.ac.uniyar.models.template.ContextAwarePebbleTemplates
import ru.ac.uniyar.models.template.ContextAwareViewRender

const val SERVER_PORT = 9000

fun main() {
    val storeHolder = try {
        StoreHolder(java.nio.file.Path.of("storage.json"), java.nio.file.Path.of("settings.json"))
    } catch (error: SettingFileError) {
        println(error.message)
        return
    }

    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender(renderer, ContentType.TEXT_HTML)

    val contexts = RequestContexts()
    val currentUserLens: RequestContextLens<User?> = RequestContextKey.optional(contexts, "user")
    htmlView.associateContextLens("currentUser", currentUserLens)

    val jwtTools = JwtTools(storeHolder.settings.salt, "ru.ac.uniyar.WebApplication")

    val handlerHolder = HttpHandlerHolder(
        currentUserLens,
        htmlView,
        storeHolder,
        jwtTools
    )

    val routingHttpHandler = static(ResourceLoader.Classpath("/ru/ac/uniyar/public/"))

    val router = Router(
        handlerHolder.showNewEntrepreneurFormHandler,
        handlerHolder.addEntrepreneurHandler,
        handlerHolder.showNewInvestmentFormHandler,
        handlerHolder.addInvestmentHandler,
        handlerHolder.showNewProjectFormHandler,
        handlerHolder.addProjectHandler,
        handlerHolder.showEntrepreneurHandler,
        handlerHolder.showEntrepreneursListHandler,
        handlerHolder.showInvestmentHandler,
        handlerHolder.showInvestmentsListHandler,
        handlerHolder.showProjectHandler,
        handlerHolder.showProjectsListHandler,
        handlerHolder.showStartPageHandler,
        routingHttpHandler
    )

    val printingApp: HttpHandler =
        ServerFilters.InitialiseRequestContext(contexts)
            .then(showErrorMessageFilter(htmlView))
            .then(router())
    val server = printingApp.asServer(Undertow(SERVER_PORT)).start()
    println("Server started on http://localhost:" + server.port())
}
