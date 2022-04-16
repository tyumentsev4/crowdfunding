package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Project

data class NewInvestmentViewModel(val form: WebForm, val projects: Iterable<Project>) : ViewModel
