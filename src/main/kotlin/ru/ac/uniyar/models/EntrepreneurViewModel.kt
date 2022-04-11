package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneur
import ru.ac.uniyar.domain.Project

data class EntrepreneurViewModel(val entrepreneur: Entrepreneur, val projects: Iterable<Project>) : ViewModel
