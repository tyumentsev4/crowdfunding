package ru.ac.uniyar.models.template

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.error.LoaderException
import com.mitchellbosecke.pebble.loader.FileLoader
import org.http4k.template.ViewModel
import org.http4k.template.ViewNotFound
import java.io.StringWriter

typealias ContextAwareTemplateRenderer = (Map<String, Any?>, ViewModel) -> String

class ContextAwarePebbleTemplates(
    private val configure: (PebbleEngine.Builder) -> PebbleEngine.Builder = { it }
) {
    private class ContextAwarePebbleTemplateRenderer(
        private val engine: PebbleEngine,
    ) : ContextAwareTemplateRenderer {
        override fun invoke(context: Map<String, Any?>, viewModel: ViewModel): String = try {
            val writer = StringWriter()
            val wholeContext = context + mapOf("model" to viewModel)
            engine.getTemplate(viewModel.template() + ".peb").evaluate(writer, wholeContext)
            writer.toString()
        } catch (_: LoaderException) {
            throw ViewNotFound(viewModel)
        }
    }

    fun hotReload(baseTemplateDir: String = "."): ContextAwareTemplateRenderer {
        val loader = FileLoader()
        loader.prefix = baseTemplateDir
        return ContextAwarePebbleTemplateRenderer(
            configure(
                PebbleEngine.Builder().cacheActive(false).loader(loader)
            ).build()
        )
    }
}
