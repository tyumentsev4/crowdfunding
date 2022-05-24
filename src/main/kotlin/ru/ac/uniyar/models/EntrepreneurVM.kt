package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.User

data class EntrepreneurVM(val entrepreneur: User, val projects: Iterable<Project>) : ViewModel
