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
import org.http4k.lens.string
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.EMPTY_UUID
import ru.ac.uniyar.domain.Entrepreneur
import ru.ac.uniyar.domain.Store
import ru.ac.uniyar.models.NewEntrepreneurViewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun entrepreneurCreationRoute(htmlView: BiDiBodyLens<ViewModel>, store: Store) = routes(
    "/" bind Method.GET to showNewEntrepreneurForm(htmlView),
    "/" bind Method.POST to addEntrepreneur(htmlView, store)
)

fun addEntrepreneur(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = { request ->
    val nameFormLens = FormField.string().required("name")
    val entrepreneurFormLens = Body.webForm(
        Validator.Feedback,
        nameFormLens
    ).toLens()

    val webForm = entrepreneurFormLens(request)
    val entrepreneursRepository = store.entrepreneursRepository
    if (webForm.errors.isEmpty()) {
        entrepreneursRepository.add(
            Entrepreneur(
                EMPTY_UUID,
                nameFormLens(webForm),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            )
        )
        Response(FOUND).header("Location", "/entrepreneurs")
    } else Response(OK).with(htmlView of NewEntrepreneurViewModel(webForm))
}

fun showNewEntrepreneurForm(htmlView: BiDiBodyLens<ViewModel>): HttpHandler = {
    Response(OK).with(htmlView of NewEntrepreneurViewModel(WebForm()))
}
