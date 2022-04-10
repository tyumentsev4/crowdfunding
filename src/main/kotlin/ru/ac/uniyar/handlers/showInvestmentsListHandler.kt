package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.domain.Investments
import ru.ac.uniyar.models.InvestmentsListViewModel

fun showInvestmentsList(renderer: TemplateRenderer, investments: Investments): HttpHandler = {
    Response(Status.OK).body(renderer(InvestmentsListViewModel(investments.getAll())))
}