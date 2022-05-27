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
import org.http4k.lens.dateTime
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.EditProjectQuery
import ru.ac.uniyar.domain.queries.FetchProjectViaIdQuery
import ru.ac.uniyar.domain.queries.FundSizeShouldBePositiveInt
import ru.ac.uniyar.domain.queries.StartTimeShouldBeLower
import ru.ac.uniyar.domain.storage.User
import ru.ac.uniyar.models.EditProjectVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowEditProjectFormHandler(
    private val htmlView: ContextAwareViewRender,
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
    }
    override fun invoke(request: Request): Response {
        val project = fetchProjectViaIdQuery.invoke(projectIdLens(request))
        val user = currentUserLens(request)
        if (project == null)
            return Response(Status.BAD_REQUEST)
        if (project.entrepreneurId != user?.id)
            return Response(Status.UNAUTHORIZED)
        val fields = mutableMapOf<String, List<String>>()
        fields["name"] = listOf(project.name)
        fields["description"] = listOf(project.description)
        fields["fundSize"] = listOf(project.fundSize.toString())
        fields["fundraisingStart"] = listOf(project.fundraisingStart.toString())
        fields["fundraisingEnd"] = listOf(project.fundraisingEnd.toString())
        return Response(Status.OK).with(htmlView(request) of EditProjectVM(WebForm(fields = fields)))
    }
}

class EditProjectHandler(
    private val htmlView: ContextAwareViewRender,
    private val editProjectQuery: EditProjectQuery,
    private val currentUserLens: RequestContextLens<User?>,
    private val fetchProjectViaIdQuery: FetchProjectViaIdQuery
) : HttpHandler {
    companion object {
        private val projectIdLens = Path.uuid().of("id")
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
        val projectId = projectIdLens(request)
        val project = fetchProjectViaIdQuery.invoke(projectId)
        val currentUser = currentUserLens(request)
        if (project == null)
            return Response(Status.BAD_REQUEST)
        if (project.entrepreneurId != currentUser?.id)
            return Response(Status.UNAUTHORIZED)

        var webForm = projectFormLens(request)
        if (webForm.errors.isEmpty()) {
            webForm = try {
                editProjectQuery.invoke(
                    projectId,
                    ProjectFromForm(
                        nameFormLens(webForm),
                        currentUser.id,
                        descriptionFormLens(webForm),
                        fundSizeFormLens(webForm),
                        fundraisingStartFormLens(webForm),
                        fundraisingEndFormLens(webForm)
                    )
                )
                return Response(Status.FOUND).header("Location", "/projects/$projectId")
            } catch (_: StartTimeShouldBeLower) {
                val newErrors = webForm.errors + Invalid(fundraisingStartFormLens.meta)
                webForm.copy(errors = newErrors)
            } catch (_: FundSizeShouldBePositiveInt) {
                val newErrors = webForm.errors + Invalid(fundSizeFormLens.meta)
                webForm.copy(errors = newErrors)
            }
        }
        return Response(Status.OK).with(htmlView(request) of EditProjectVM(webForm))
    }
}
