package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.models.StartPageViewModel

fun showStartPage(renderer: TemplateRenderer): HttpHandler = {
    val viewModel = StartPageViewModel(0)
    Response(Status.OK).body(renderer(viewModel))
}