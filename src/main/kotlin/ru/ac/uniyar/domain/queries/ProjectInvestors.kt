package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.User

data class ProjectInvestors(
    val project: Project,
    val investors: List<InvestorInfo>
)
