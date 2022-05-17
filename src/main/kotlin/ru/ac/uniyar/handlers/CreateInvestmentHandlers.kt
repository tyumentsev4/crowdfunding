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
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.AddInvestmentQuery
import ru.ac.uniyar.domain.queries.AmountShouldBePositiveInt
import ru.ac.uniyar.domain.queries.ListOpenProjectsQuery
import ru.ac.uniyar.domain.queries.ProjectNotFound
import ru.ac.uniyar.models.NewInvestmentViewModel

class ShowNewInvestmentFormHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listOpenProjectsQuery: ListOpenProjectsQuery
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val projects = listOpenProjectsQuery.invoke()
        return Response(Status.OK).with(htmlView of NewInvestmentViewModel(WebForm(), projects))
    }
}

class AddInvestmentHandler(
    private val htmlView: BiDiBodyLens<ViewModel>,
    private val listOpenProjectsQuery: ListOpenProjectsQuery,
    private val addInvestmentQuery: AddInvestmentQuery
) : HttpHandler {
    companion object {
        private val projectIdFormLens = FormField.uuid().required("projectId")
        private val investorFormLens = FormField.string().required("investorName")
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
                val uuid = addInvestmentQuery.invoke(
                    projectIdFormLens(webForm),
                    investorFormLens(webForm),
                    contactFormLens(webForm),
                    amountFormLens(webForm)
                )
                return Response(Status.FOUND).header("Location", "/investments/$uuid")
            } catch (_: ProjectNotFound) {
                val newErrors = webForm.errors + Invalid(projectIdFormLens.meta)
                webForm.copy(errors = newErrors)
            } catch (_: AmountShouldBePositiveInt) {
                val newErrors = webForm.errors + Invalid(amountFormLens.meta)
                webForm.copy(errors = newErrors)
            }
        }
        val projects = listOpenProjectsQuery.invoke()
        return Response(Status.OK).with(htmlView of NewInvestmentViewModel(webForm, projects))
    }
}
