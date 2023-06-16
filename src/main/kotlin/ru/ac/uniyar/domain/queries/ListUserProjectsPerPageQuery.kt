package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.InvestmentsRepository
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.ProjectsRepository
import java.util.UUID

class ListUserProjectsPerPageQuery(
    private val projectsRepository: ProjectsRepository,
    private val investmentsRepository: InvestmentsRepository
) {

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        pageNumber: Int,
        userId: UUID
    ): PagedResult<UserProjectInfo> {
        val investments = investmentsRepository.list()
        val list = projectsRepository.list().filter {
            it.entrepreneurId == userId
        }
        val subList = list.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        val infoList = subList.map { project: Project ->
            UserProjectInfo(
                project,
                hasProjectInvestments(project, investments)
            )
        }
        return PagedResult(infoList, countPageNumbers(list.size, PAGE_SIZE))
    }
}
