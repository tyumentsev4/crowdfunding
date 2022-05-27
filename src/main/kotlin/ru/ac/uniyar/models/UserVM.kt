package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.ProjectInvestments
import ru.ac.uniyar.domain.storage.User

data class UserVM(
    val user: User,
    val projectsInvestments: List<ProjectInvestments>,
    val isEntrepreneur: Boolean
) : ViewModel
