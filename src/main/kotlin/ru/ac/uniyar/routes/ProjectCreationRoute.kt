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
import org.http4k.lens.dateTime
import org.http4k.lens.double
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.EMPTY_UUID
import ru.ac.uniyar.domain.Project
import ru.ac.uniyar.domain.Store
import ru.ac.uniyar.models.NewProjectViewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun projectCreationRoute(htmlView: BiDiBodyLens<ViewModel>, store: Store) = routes(
    "/" bind Method.GET to showNewProjectForm(htmlView, store),
    "/" bind Method.POST to addProject(htmlView, store)
)

fun showNewProjectForm(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = {
    val repository = store.entrepreneursRepository
    val entrepreneurs = repository.fetchAll()
    Response(OK).with(htmlView of NewProjectViewModel(WebForm(), entrepreneurs))
}

fun addProject(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = { request ->
    val nameFormLens = FormField.string().required("name")
    val entrepreneurIdFormLens = FormField.uuid().required("entrepreneur")
    val descriptionFormLens = FormField.string().required("description")
    val fundSizeFormLens = FormField.double().required("fundSize")
    val fundraisingStartFormLens = FormField.dateTime().required("fundraisingStart")
    val fundraisingEndFormLens = FormField.dateTime().required("fundraisingEnd")
    val projectFormLens = Body.webForm(
        Validator.Feedback,
        nameFormLens,
        entrepreneurIdFormLens,
        fundSizeFormLens,
        fundraisingStartFormLens,
        fundraisingEndFormLens,
    ).toLens()

    val webForm = projectFormLens(request)
    val projectRepository = store.projectsRepository
    val entrepreneursRepository = store.entrepreneursRepository
    if (webForm.errors.isEmpty()) {
        projectRepository.add(
            Project(
                EMPTY_UUID,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                nameFormLens(webForm),
                entrepreneurIdFormLens(webForm),
                descriptionFormLens(webForm),
                fundSizeFormLens(webForm),
                fundraisingStartFormLens(webForm),
                fundraisingEndFormLens(webForm)
            )
        )
        Response(FOUND).header("Location", "/projects")
    } else Response(OK).with(htmlView of NewProjectViewModel(webForm, entrepreneursRepository.fetchAll()))
}
