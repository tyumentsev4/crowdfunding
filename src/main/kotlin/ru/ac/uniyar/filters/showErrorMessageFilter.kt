package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.models.ShowErrorInfoViewModel

fun showErrorMessageFilter(renderer: TemplateRenderer): Filter = Filter { next: HttpHandler ->
    { request ->
        val response = next(request)
        if (response.status.successful) {
            response
        } else {
            response.body(renderer(ShowErrorInfoViewModel(request.uri)))
        }
    }
}
