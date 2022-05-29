package ru.ac.uniyar.domain.storage

import org.http4k.format.Jackson
import java.nio.file.Path
import kotlin.io.path.isReadable

class Settings(settingsPath: Path) {
    val salt: String

    init {
        if (!settingsPath.isReadable()) throw SettingFileError("Configuration file $settingsPath doesn't exist")

        val file = settingsPath.toFile()
        val jsonDocument = file.readText()
        val node = Jackson.parse(jsonDocument)

        if (!node.hasNonNull("salt")) throw SettingFileError("Configuration file doesn't have salt")

        salt = node["salt"].asText()
    }
}

class SettingFileError(message: String) : RuntimeException(message)
