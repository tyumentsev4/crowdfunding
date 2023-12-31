package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.ProjectInfo

data class ProjectVM(
    val projectInfo: ProjectInfo
) : ViewModel
