package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.models.StartPageVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowStartPageHandler(
    private val htmlView: ContextAwareViewRender
) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response(Status.OK).with(htmlView(request) of StartPageVM(0))
    }
}
