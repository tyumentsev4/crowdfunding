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
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.AddInvestmentQuery
import ru.ac.uniyar.domain.queries.AmountShouldBePositiveInt
import ru.ac.uniyar.domain.queries.ListProjectsQuery
import ru.ac.uniyar.domain.queries.ProjectFetchError
import ru.ac.uniyar.domain.queries.ProjectNotFound
import ru.ac.uniyar.models.NewInvestmentViewModel

class ShowNewInvestmentFormHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listProjectsQuery: ListProjectsQuery
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val projects = listProjectsQuery.invoke()
        return Response(Status.OK).with(htmlView of NewInvestmentViewModel(WebForm(), projects))
    }
}

class AddInvestmentHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listProjectsQuery: ListProjectsQuery,
    private val addInvestmentQuery: AddInvestmentQuery
) : HttpHandler {
    companion object {
        private val projectIdFormLens = FormField.uuid().required("projectId")
        private val investorFormLens = FormField.nonEmptyString().required("investorName")
        private val contactFormLens = FormField.string().required("contactInfo")
        private val amountFormLens = FormField.int().required("amount")
        private val investmentFormLens = Body.webForm(
            Validator.Feedback,
            projectIdFormLens,
            investorFormLens,
            contactFormLens,
            amountFormLens
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        var webForm = investmentFormLens(request)
        if (webForm.errors.isEmpty()) {
            webForm = try {
                addInvestmentQuery.invoke(
                    projectIdFormLens(webForm),
                    investorFormLens(webForm),
                    contactFormLens(webForm),
                    amountFormLens(webForm)
                )
                return Response(Status.FOUND).header("Location", "/investments")
            } catch (_: ProjectNotFound) {
                val newErrors = webForm.errors + Invalid(projectIdFormLens.meta)
                webForm.copy(errors = newErrors)
            } catch (_: AmountShouldBePositiveInt) {
                val newErrors = webForm.errors + Invalid(amountFormLens.meta)
                webForm.copy(errors = newErrors)
            }
        }
        val projects = listProjectsQuery.invoke()
        return Response(Status.OK).with(htmlView of NewInvestmentViewModel(webForm, projects))
    }
}
