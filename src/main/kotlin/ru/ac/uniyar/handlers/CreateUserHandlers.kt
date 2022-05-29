package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.AddUserQuery
import ru.ac.uniyar.models.NewUserVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowNewUserFormHandler(private val htmlView: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlView(request) of NewUserVM(WebForm()))
    }
}

class AddUserHandler(
    private val htmlView: ContextAwareViewRender,
    private val addUserQuery: AddUserQuery
) : HttpHandler {
    companion object {
        private val nameFormLens = FormField.nonEmptyString().required("name")
        private val passwordOneFormLens = FormField.nonEmptyString().required("passwordOne")
        private val passwordTwoFormLens = FormField.nonEmptyString().required("passwordTwo")
        private val contactInfoLens = FormField.nonEmptyString().required("contactInfo")
        val userFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens,
            passwordOneFormLens,
            passwordTwoFormLens,
            contactInfoLens
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        var webForm = userFormLens(request)
        val firstPassword = lensOrNull(passwordOneFormLens, webForm)
        val secondPassword = lensOrNull(passwordTwoFormLens, webForm)
        if (firstPassword != null && firstPassword != secondPassword) {
            val newErrors =
                webForm.errors + Invalid(passwordOneFormLens.meta.copy(description = "passwords don't match"))
            webForm = webForm.copy(errors = newErrors)
        }

        if (webForm.errors.isEmpty()) {
            addUserQuery.invoke(nameFormLens(webForm), firstPassword!!, contactInfoLens(webForm))
            return Response(Status.FOUND).header("Location", "/login")
        }
        return Response(Status.OK).with(htmlView(request) of NewUserVM(webForm))
    }
}
