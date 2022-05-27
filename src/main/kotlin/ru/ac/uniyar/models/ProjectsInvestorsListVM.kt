package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.ProjectInvestors

data class ProjectsInvestorsListVM(
    val projectsInvestors: List<ProjectInvestors>
): ViewModel
