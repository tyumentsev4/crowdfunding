package ru.ac.uniyar.models.template

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.error.LoaderException
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import com.mitchellbosecke.pebble.loader.FileLoader
import org.http4k.template.ViewModel
import org.http4k.template.ViewNotFound
import java.io.StringWriter

typealias ContextAwareTemplateRenderer = (Map<String, Any?>, ViewModel) -> String

class ContextAwarePebbleTemplates(
    private val configure: (PebbleEngine.Builder) -> PebbleEngine.Builder = { it },
    private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
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

    fun CachingClasspath(baseClasspathPackage: String): ContextAwareTemplateRenderer {
        val loader = ClasspathLoader(classLoader)
        loader.prefix = if (baseClasspathPackage.isEmpty()) null else baseClasspathPackage.replace('.', '/')
        return ContextAwarePebbleTemplateRenderer(configure(PebbleEngine.Builder().loader(loader)).build())
    }

    fun Caching(baseTemplateDir: String): ContextAwareTemplateRenderer {
        val loader = FileLoader()
        loader.prefix = baseTemplateDir
        return ContextAwarePebbleTemplateRenderer(
            configure(
                PebbleEngine.Builder().cacheActive(true).loader(loader)
            ).build()
        )
    }

    fun HotReload(baseTemplateDir: String): ContextAwareTemplateRenderer {
        val loader = FileLoader()
        loader.prefix = baseTemplateDir
        return ContextAwarePebbleTemplateRenderer(
            configure(
                PebbleEngine.Builder().cacheActive(false).loader(loader)
            ).build()
        )
    }
}
