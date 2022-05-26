package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository

class InvestmentsByProjectQuery(
    private val investmentRepository: InvestmentsRepository,
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke() : Map<Project, List<Investment>> {
        val investments = investmentRepository.list()
        val projects = projectsRepository.list()

        val investmentsByProjectMap = mutableMapOf<Project, MutableList<Investment>>()
        projects.forEach { project: Project ->
            investmentsByProjectMap[project] = mutableListOf()
            val i = investments.filter { it.projectId == project.id }
            i.groupByTo(investmentsByProjectMap, { project }, { it })
        }
        return investmentsByProjectMap
    }
}
