package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.dateTime
import org.http4k.lens.double
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.AddProjectQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursQuery
import ru.ac.uniyar.domain.queries.StartTimeShouldBeLower
import ru.ac.uniyar.models.NewProjectViewModel

class ShowNewProjectFormHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listEntrepreneursQuery: ListEntrepreneursQuery
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val entrepreneurs = listEntrepreneursQuery.invoke()
        return Response(Status.OK).with(htmlView of NewProjectViewModel(WebForm(), entrepreneurs))
    }
}

class AddProjectHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listEntrepreneursQuery: ListEntrepreneursQuery,
    private val addProjectQuery: AddProjectQuery
) : HttpHandler {
    companion object {
        private val nameFormLens = FormField.string().required("name")
        private val entrepreneurIdFormLens = FormField.uuid().required("entrepreneur")
        private val descriptionFormLens = FormField.string().required("description")
        private val fundSizeFormLens = FormField.double().required("fundSize")
        private val fundraisingStartFormLens = FormField.dateTime().required("fundraisingStart")
        private val fundraisingEndFormLens = FormField.dateTime().required("fundraisingEnd")
        private val projectFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens,
            entrepreneurIdFormLens,
            fundSizeFormLens,
            fundraisingStartFormLens,
            fundraisingEndFormLens,
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        var webForm = projectFormLens(request)
        if (webForm.errors.isEmpty()) {
            try {
                addProjectQuery.invoke(
                    nameFormLens(webForm),
                    entrepreneurIdFormLens(webForm),
                    descriptionFormLens(webForm),
                    fundSizeFormLens(webForm),
                    fundraisingStartFormLens(webForm),
                    fundraisingEndFormLens(webForm)
                )
                return Response(Status.FOUND).header("Location", "/projects")
            } catch (_: StartTimeShouldBeLower) {
                val newErrors = webForm.errors + Invalid(fundraisingStartFormLens.meta.copy(description = "Invalid"))
                webForm = webForm.copy(errors = newErrors)
            }
        }
        val entrepreneurs = listEntrepreneursQuery.invoke()
        return Response(Status.OK).with(htmlView of NewProjectViewModel(webForm, entrepreneurs))
    }
}
