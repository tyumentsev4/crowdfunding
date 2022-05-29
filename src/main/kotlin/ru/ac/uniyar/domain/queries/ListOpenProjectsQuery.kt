package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.ProjectsRepository

class ListOpenProjectsQuery(private val projectsRepository: ProjectsRepository) {
    operator fun invoke() = projectsRepository.list().filter { it.isOpen() }
}
