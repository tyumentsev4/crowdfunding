package ru.ac.uniyar.domain

class Projects {
    private val projects = mutableListOf<Project>()

    fun add(project: Project) {
        projects.add(project)
    }

    fun getAllProjects(): Iterable<IndexedValue<Project>> {
        return projects.withIndex()
    }

    fun getProject(index: Int): Project {
        return projects[index]
    }
}