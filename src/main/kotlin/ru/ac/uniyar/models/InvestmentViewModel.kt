package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Investment
import ru.ac.uniyar.domain.storage.Project

data class InvestmentViewModel(val investment: Investment, val project: Project) : ViewModel
