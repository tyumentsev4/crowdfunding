package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.UserProjectInfo

data class UserProjectsListVM(
    val projects: List<UserProjectInfo>,
    val paginator: Paginator,
) : ViewModel
