package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.User

data class EntrepreneurInfo(
    val entrepreneur: User,
    val projects: List<Project>
)
