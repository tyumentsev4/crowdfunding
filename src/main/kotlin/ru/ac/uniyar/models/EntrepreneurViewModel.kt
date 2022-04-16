package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.Project

data class EntrepreneurViewModel(val entrepreneur: Entrepreneur, val projects: Iterable<Project>) : ViewModel
