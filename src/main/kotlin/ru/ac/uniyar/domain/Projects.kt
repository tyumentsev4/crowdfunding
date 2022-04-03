package ru.ac.uniyar.domain

class Projects {
    private val projectsList = mutableListOf<Project>()

    fun add(project: Project) {
        projectsList.add(project)
    }

    fun getAllProjects(): Iterable<IndexedValue<Project>> {
        return projectsList.withIndex()
    }

    fun getProject(index: Int): Project? {
        return projectsList.getOrNull(index)
    }
}
