package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Project

data class InvestmentInfo(
    val investment: Investment,
    val project: Project
)
