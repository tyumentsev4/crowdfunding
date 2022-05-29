package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Project

data class ProjectInvestments(
    val project: Project,
    val investments: List<Investment>
)
