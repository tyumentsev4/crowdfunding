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
import org.http4k.lens.double
import org.http4k.lens.string
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Investment
import ru.ac.uniyar.domain.Investments
import ru.ac.uniyar.domain.Projects
import ru.ac.uniyar.models.NewEntrepreneurViewModel
import ru.ac.uniyar.models.NewInvestmentViewModel
import java.time.LocalDateTime

fun investmentsCreationRoute(investments: Investments, projects: Projects, htmlView: BiDiBodyLens<ViewModel>) = routes(
    "/" bind Method.GET to showNewInvestmentForm(htmlView, projects),
    "/" bind Method.POST to addInvestment(investments, htmlView)
)

fun addInvestment(investments: Investments, htmlView: BiDiBodyLens<ViewModel>): HttpHandler = { request ->
    val projectFormLens = FormField.string().required("project")
    val investorFormLens = FormField.string().required("investorName")
    val contactFormLens = FormField.string().required("contactInfo")
    val amountFormLens = FormField.double().required("amount")
    val entrepreneurFormLens = Body.webForm(
        Validator.Feedback,
        projectFormLens,
        investorFormLens,
        contactFormLens,
        amountFormLens
    ).toLens()

    val webForm = entrepreneurFormLens(request)
    if (webForm.errors.isEmpty()) {
        investments.add(
            Investment(
                investments.size(),
                LocalDateTime.now(),
                projectFormLens(webForm),
                investorFormLens(webForm),
                contactFormLens(webForm),
                amountFormLens(webForm)
            )
        )
        Response(FOUND).header("Location", "/investments")
    } else Response(OK).with(htmlView of NewEntrepreneurViewModel(webForm))
}

fun showNewInvestmentForm(htmlView: BiDiBodyLens<ViewModel>, projects: Projects): HttpHandler = {
    Response(OK).with(htmlView of NewInvestmentViewModel(WebForm(), projects.getAll()))
}
