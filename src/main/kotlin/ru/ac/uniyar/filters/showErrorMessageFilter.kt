package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.with
import ru.ac.uniyar.models.ShowErrorInfoVM
import ru.ac.uniyar.models.template.ContextAwareViewRender

fun showErrorMessageFilter(htmlView: ContextAwareViewRender): Filter =
    Filter { next: HttpHandler ->
        { request ->
            val response = next(request)
            if (response.status.successful) {
                response
            } else {
                response.with(htmlView(request) of ShowErrorInfoVM(request.uri))
            }
        }
}
