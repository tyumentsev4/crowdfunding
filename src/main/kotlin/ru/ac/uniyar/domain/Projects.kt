package ru.ac.uniyar.domain

class Projects {
    private val projectsList = mutableListOf<Project>()

    fun size(): Int{
        return projectsList.size
    }

    fun add(project: Project) {
        projectsList.add(project)
    }

    fun getAll(): Iterable<Project> {
        return projectsList
    }

    fun getProject(index: Int): Project? {
        return projectsList.getOrNull(index)
    }
}
