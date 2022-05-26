package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project

data class UserProjectInfo(
    val project: Project,
    val hasInvestments: Boolean
)
