package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Project

data class ProjectInvestors(
    val project: Project,
    val investors: List<InvestorInfo>
)
