package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.models.ProjectsListViewModel

fun showProjectsList(renderer: TemplateRenderer, projects: Projects): HttpHandler = {
    val viewModel = ProjectsListViewModel(projects.getAll())
    Response(Status.OK).body(renderer(viewModel))
}