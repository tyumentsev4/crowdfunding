package ru.ac.uniyar.routes

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneurs
import ru.ac.uniyar.domain.Project
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.models.NewProjectViewModel
import java.time.LocalDateTime

fun projectCreationRoute(projects: Projects, htmlView: BiDiBodyLens<ViewModel>, entrepreneurs: Entrepreneurs) = routes(
    "/" bind Method.GET to showNewProjectForm(htmlView, entrepreneurs),
    "/" bind Method.POST to addProject(projects, htmlView, entrepreneurs)
)

fun showNewProjectForm(htmlView: BiDiBodyLens<ViewModel>, entrepreneurs: Entrepreneurs): HttpHandler = {
    Response(OK).with(htmlView of NewProjectViewModel(WebForm(), entrepreneurs.getAll()))
}

fun addProject(projects: Projects, htmlView: BiDiBodyLens<ViewModel>, entrepreneurs: Entrepreneurs): HttpHandler = { request ->
    val nameFormLens = FormField.string().required("name")
    val entrepreneurFormLens = FormField.string().required("entrepreneur")
    val descriptionFormLens = FormField.string().required("description")
    val fundSizeFormLens = FormField.int().required("fundSize")
    val fundraisingStartFormLens = FormField.required("fundraisingStart")
    val fundraisingEndFormLens = FormField.required("fundraisingEnd")
    val projectFormLens = Body.webForm(
        Validator.Feedback,
        nameFormLens,
        entrepreneurFormLens,
        fundSizeFormLens,
        fundraisingStartFormLens,
        fundraisingEndFormLens,
    ).toLens()

    val webForm = projectFormLens(request)
    if (webForm.errors.isEmpty()) {
        projects.add(Project(
            projects.size(),
            LocalDateTime.now(),
            nameFormLens(webForm),
            entrepreneurFormLens(webForm),
            descriptionFormLens(webForm),
            fundSizeFormLens(webForm),
            LocalDateTime.parse(fundraisingStartFormLens(webForm)),
            LocalDateTime.parse(fundraisingEndFormLens(webForm))
        ))
        Response(FOUND).header("Location", "/projects")
    } else Response(OK).with(htmlView of NewProjectViewModel(webForm, entrepreneurs.getAll()))
}
