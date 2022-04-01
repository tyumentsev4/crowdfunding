package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Project

data class ProjectsListViewModel(val projects: Iterable<IndexedValue<Project>>) : ViewModel
