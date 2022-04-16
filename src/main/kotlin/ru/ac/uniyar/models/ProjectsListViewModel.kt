package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Project

data class ProjectsListViewModel(val projects: Iterable<Project>, val paginator: Paginator) : ViewModel
