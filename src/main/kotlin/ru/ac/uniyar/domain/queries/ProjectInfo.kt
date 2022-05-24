package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.User

data class ProjectInfo(
    val project: Project,
    val entrepreneur: User,
    val lastInvestments: List<Investment>,
    val investmentsSize: Int,
    val nonAnonymousInvestmentsNumber: Int,
    val isSuccessfulProject: Boolean?,
    val isProjectSuccessForecast: Boolean,
    val necessaryInvestmentsToProject: Int,
    val daysUntilTheEndOfProject: Int,
    val isProjectOpen: Boolean
)
