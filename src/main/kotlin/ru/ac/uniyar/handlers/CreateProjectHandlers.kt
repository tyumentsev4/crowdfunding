package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.dateTime
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.AddProjectQuery
import ru.ac.uniyar.domain.queries.FundSizeShouldBePositiveInt
import ru.ac.uniyar.domain.queries.StartTimeShouldBeLower
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.NewProjectVM
import ru.ac.uniyar.models.template.ContextAwareViewRender
import java.time.LocalDateTime
import java.util.UUID

class ShowNewProjectFormHandler(
    private val htmlView: ContextAwareViewRender,
    private val permissionsLens: RequestContextLens<RolePermissions>
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.openNewProject)
            return Response(Status.UNAUTHORIZED)

        return Response(Status.OK).with(htmlView(request) of NewProjectVM(WebForm()))
    }
}

data class ProjectFromForm(
    val name: String,
    val userId: UUID,
    val description: String,
    val fundSize: Int,
    val fundraisingStart: LocalDateTime,
    val fundraisingEnd: LocalDateTime
)

class AddProjectHandler(
    private val htmlView: ContextAwareViewRender,
    private val addProjectQuery: AddProjectQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val currentUserLens: RequestContextLens<User?>
) : HttpHandler {
    companion object {
        private val nameFormLens = FormField.nonEmptyString().required("name")
        private val descriptionFormLens = FormField.string().required("description")
        private val fundSizeFormLens = FormField.int().required("fundSize")
        private val fundraisingStartFormLens = FormField.dateTime().required("fundraisingStart")
        private val fundraisingEndFormLens = FormField.dateTime().required("fundraisingEnd")
        private val projectFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens,
            fundSizeFormLens,
            fundraisingStartFormLens,
            fundraisingEndFormLens,
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.openNewProject)
            return Response(Status.UNAUTHORIZED)

        val currentUser = currentUserLens(request) ?: return Response(Status.UNAUTHORIZED)

        var webForm = projectFormLens(request)
        if (webForm.errors.isEmpty()) {
            webForm = try {
                val uuid = addProjectQuery.invoke(
                    ProjectFromForm(
                        nameFormLens(webForm),
                        currentUser.id,
                        descriptionFormLens(webForm),
                        fundSizeFormLens(webForm),
                        fundraisingStartFormLens(webForm),
                        fundraisingEndFormLens(webForm)
                    )
                )
                return Response(Status.FOUND).header("Location", "/projects/$uuid")
            } catch (_: StartTimeShouldBeLower) {
                val newErrors = webForm.errors + Invalid(fundraisingStartFormLens.meta)
                webForm.copy(errors = newErrors)
            } catch (_: FundSizeShouldBePositiveInt) {
                val newErrors = webForm.errors + Invalid(fundSizeFormLens.meta)
                webForm.copy(errors = newErrors)
            }
        }
        return Response(Status.OK).with(htmlView(request) of NewProjectVM(webForm))
    }
}
