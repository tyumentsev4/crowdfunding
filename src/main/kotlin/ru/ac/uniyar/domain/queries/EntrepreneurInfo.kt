package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.Project

data class EntrepreneurInfo(
    val entrepreneur: Entrepreneur,
    val projects: List<Project>
)
