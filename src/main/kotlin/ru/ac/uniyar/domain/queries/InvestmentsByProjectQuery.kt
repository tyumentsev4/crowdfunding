package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository

class InvestmentsByProjectQuery(
    private val investmentRepository: InvestmentsRepository,
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(): List<ProjectInvestments> {
        val investments = investmentRepository.list()
        val projects = projectsRepository.list()
        val projectsInvestments = mutableListOf<ProjectInvestments>()

        projects.forEach { project: Project ->
            val projectInvestments = investments.filter { it.projectId == project.id }
            projectsInvestments.add(ProjectInvestments(project, projectInvestments))
        }
        return projectsInvestments
    }
}
