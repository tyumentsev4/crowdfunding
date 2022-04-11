package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.LensFailure
import org.http4k.lens.Path
import org.http4k.lens.uuid
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Store
import ru.ac.uniyar.models.InvestmentViewModel

fun showInvestment(htmlView: BiDiBodyLens<ViewModel>, store: Store): HttpHandler = handler@{ request ->
    val idLens = Path.uuid().of("id")
    val id = try {
        idLens(request)
    } catch (error: LensFailure) {
        return@handler Response(Status.BAD_REQUEST).header("error", error.toString())
    }
    val investmentRepository = store.investmentsRepository
    val projectRepository = store.projectsRepository
    val investment = investmentRepository.fetch(id) ?: return@handler Response(Status.BAD_REQUEST)
    val project = projectRepository.fetch(investment.projectId) ?: return@handler Response(Status.BAD_REQUEST)
    Response(Status.OK).with(htmlView of InvestmentViewModel(investment, project))
}
