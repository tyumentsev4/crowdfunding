package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.models.ProjectViewModel

fun showProject(renderer: TemplateRenderer, projects: Projects): HttpHandler = handler@{ request ->
    val indexString = request.path("index").orEmpty()
    val index = indexString.toIntOrNull() ?: return@handler Response(Status.BAD_REQUEST)
    val project = projects.getProject(index) ?: return@handler Response(Status.BAD_REQUEST)
    val viewModel = ProjectViewModel(project)
    Response(Status.OK).body(renderer(viewModel))
}