package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.template.ViewModel
import ru.ac.uniyar.models.StartPageViewModel

fun showStartPage(htmlView: BiDiBodyLens<ViewModel>): HttpHandler = {
    val viewModel = StartPageViewModel(0)
    Response(Status.OK).with(htmlView of viewModel)
}
