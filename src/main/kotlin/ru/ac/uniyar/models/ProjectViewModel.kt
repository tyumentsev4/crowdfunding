package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneur
import ru.ac.uniyar.domain.Investments
import ru.ac.uniyar.domain.Project

data class ProjectViewModel(
    val project: Project,
    val entrepreneur: Entrepreneur,
    val investments: Investments
) : ViewModel
