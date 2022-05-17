package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.AddEntrepreneurQuery
import ru.ac.uniyar.models.NewEntrepreneurViewModel

class ShowNewEntrepreneurFormHandler(private val htmlView: BiDiBodyLens<ViewModel>) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlView of NewEntrepreneurViewModel(WebForm()))
    }
}

class AddEntrepreneurHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val addEntrepreneurQuery: AddEntrepreneurQuery
) : HttpHandler {
    companion object {
        private val nameFormLens = FormField.nonEmptyString().required("name")
        val entrepreneurFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        val webForm = entrepreneurFormLens(request)
        if (webForm.errors.isEmpty()) {
            val uuid = addEntrepreneurQuery.invoke(nameFormLens(webForm))
            return Response(Status.FOUND).header("Location", "/entrepreneurs/$uuid")
        }
        return Response(Status.OK).with(htmlView of NewEntrepreneurViewModel(webForm))
    }
}
