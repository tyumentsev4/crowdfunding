package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Project

fun hasProjectInvestments(project: Project, investments: List<Investment>): Boolean {
    return investments.any { it.projectId == project.id }
}
