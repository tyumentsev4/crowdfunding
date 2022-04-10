package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.domain.Entrepreneurs
import ru.ac.uniyar.models.EntrepreneursListViewModel

fun showEntrepreneursList(renderer: TemplateRenderer, entrepreneurs: Entrepreneurs): HttpHandler = {
    Response(Status.OK).body(renderer(EntrepreneursListViewModel(entrepreneurs.getAll())))
}