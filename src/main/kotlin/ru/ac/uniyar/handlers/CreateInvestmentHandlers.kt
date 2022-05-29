package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.boolean
import org.http4k.lens.int
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.AddInvestmentQuery
import ru.ac.uniyar.domain.queries.AmountShouldBePositiveInt
import ru.ac.uniyar.domain.queries.FetchProjectViaIdQuery
import ru.ac.uniyar.domain.queries.ListOpenProjectsQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.NewInvestmentVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowNewInvestmentFormHandler(
    private val htmlView: ContextAwareViewRender,
    private val listOpenProjectsQuery: ListOpenProjectsQuery,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>,
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
    }
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.addInvestment)
            return Response(Status.UNAUTHORIZED)
        val openProjects = listOpenProjectsQuery.invoke()
        val projectId = lensOrNull(projectIdLens, request) ?: return Response(Status.BAD_REQUEST)
        val project = fetchProjectViaIdQuery.invoke(projectId) ?: return Response(Status.BAD_REQUEST)
        if (!openProjects.contains(project)) return Response(Status.BAD_REQUEST)
        return Response(Status.OK).with(htmlView(request) of NewInvestmentVM(WebForm(), project))
    }
}

class AddInvestmentHandler(
    private val htmlView: ContextAwareViewRender,
    private val listOpenProjectsQuery: ListOpenProjectsQuery,
    private val addInvestmentQuery: AddInvestmentQuery,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery,
    private val currentUserLens: RequestContextLens<User?>,
    private val permissionsLens: RequestContextLens<RolePermissions>
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
        private val isAnonInvestmentLens = FormField.boolean().defaulted("isAnonInvestment", false)
        private val amountFormLens = FormField.int().required("amount")
        private val investmentFormLens = Body.webForm(
            Validator.Feedback,
            amountFormLens
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.addInvestment)
            return Response(Status.UNAUTHORIZED)

        val projectId = lensOrNull(projectIdLens, request) ?: return Response(Status.BAD_REQUEST)
        val openProjects = listOpenProjectsQuery.invoke()
        val project = fetchProjectViaIdQuery.invoke(projectId) ?: return Response(Status.BAD_REQUEST)
        if (!openProjects.contains(project)) return Response(Status.BAD_REQUEST)

        var webForm = investmentFormLens(request)
        if (webForm.errors.isEmpty()) {
            var investorName = ""
            var investorContactInfo = ""
            val currentUser = currentUserLens(request)!!
            if (!isAnonInvestmentLens(webForm)) {
                investorName = currentUser.name
                investorContactInfo = currentUser.contactInfo
            }
            webForm = try {
                val uuid = addInvestmentQuery.invoke(
                    projectId,
                    currentUser.id,
                    investorName,
                    investorContactInfo,
                    amountFormLens(webForm)
                )
                return Response(Status.FOUND).header("Location", "/investments/$uuid")
            } catch (_: AmountShouldBePositiveInt) {
                val newErrors = webForm.errors + Invalid(amountFormLens.meta)
                webForm.copy(errors = newErrors)
            }
        }
        return Response(Status.OK).with(htmlView(request) of NewInvestmentVM(webForm, project))
    }
}
