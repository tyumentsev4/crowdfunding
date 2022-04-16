package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.Investments
import ru.ac.uniyar.domain.storage.Project

data class ProjectInfo(
    val project: Project,
    val entrepreneur: Entrepreneur,
    val investments: Investments
)
