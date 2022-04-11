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
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.EMPTY_UUID
import ru.ac.uniyar.domain.Investment
import ru.ac.uniyar.domain.Store
import ru.ac.uniyar.models.NewInvestmentViewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun investmentsCreationRoute(htmlView: BiDiBodyLens<ViewModel>, store: Store) = routes(
    "/" bind Method.GET to showNewInvestmentForm(htmlView, store),
    "/" bind Method.POST to addInvestment(htmlView, store)
)

fun addInvestment(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = { request ->
    val projectFormLens = FormField.uuid().required("projectId")
    val investorFormLens = FormField.string().required("investorName")
    val contactFormLens = FormField.string().required("contactInfo")
    val amountFormLens = FormField.double().required("amount")
    val investmentFormLens = Body.webForm(
        Validator.Feedback,
        projectFormLens,
        investorFormLens,
        contactFormLens,
        amountFormLens
    ).toLens()

    val webForm = investmentFormLens(request)
    val investmentsRepository = store.investmentsRepository
    val projectsRepository = store.projectsRepository
    if (webForm.errors.isEmpty()) {
        investmentsRepository.add(
            Investment(
                EMPTY_UUID,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                projectFormLens(webForm),
                investorFormLens(webForm),
                contactFormLens(webForm),
                amountFormLens(webForm)
            )
        )
        Response(FOUND).header("Location", "/investments")
    } else Response(OK).with(htmlView of NewInvestmentViewModel(webForm, projectsRepository.fetchAll()))
}

fun showNewInvestmentForm(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = {
    val projectsRepository = store.projectsRepository
    Response(OK).with(htmlView of NewInvestmentViewModel(WebForm(), projectsRepository.fetchAll()))
}
