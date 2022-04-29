package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.Investments
import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.Project

data class ProjectViewModel(
    val project: Project,
    val entrepreneur: Entrepreneur,
    val investments: Investments
) : ViewModel
