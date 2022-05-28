package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.FetchInvestmentQuery
import ru.ac.uniyar.domain.queries.InvestmentFetchError
import ru.ac.uniyar.domain.queries.ProjectFetchError
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.InvestmentVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowInvestmentHandler(
    private val htmlView: ContextAwareViewRender,
    private val fetchInvestmentQuery: FetchInvestmentQuery,
) : HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val id = lensOrNull(idLens, request) ?: return Response(Status.BAD_REQUEST)
        return try {
            val investmentInfo = fetchInvestmentQuery.invoke(id)
            Response(Status.OK).with(
                htmlView(request) of InvestmentVM(investmentInfo.investment, investmentInfo.project)
            )
        } catch (_: ProjectFetchError) {
            Response(Status.BAD_REQUEST)
        } catch (_: InvestmentFetchError) {
            Response(Status.BAD_REQUEST)
        }
    }
}
