package ru.ac.uniyar

import ru.ac.uniyar.models.PebbleViewModel
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import org.http4k.template.viewModel


fun respondWithPong(): HttpHandler = {
    Response(OK).body("pong")
}

fun showTemplate(renderer: TemplateRenderer): HttpHandler = {
    val viewModel = PebbleViewModel("Hello there!")
    Response(OK).body(renderer(viewModel))
}

fun app(renderer: TemplateRenderer): HttpHandler = routes(
    "/ping" bind GET to respondWithPong(),
    "/templates/pebble" bind GET to showTemplate(renderer),
    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),
)

fun main() {
    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val printingApp: HttpHandler = PrintRequestAndResponse().then(app(renderer))
    val server = printingApp.asServer(Undertow(9000)).start()
    println("Server started on http://localhost:" + server.port())
}
