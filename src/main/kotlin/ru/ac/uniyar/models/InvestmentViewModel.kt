package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Investment

data class InvestmentViewModel(val investment: Investment) : ViewModel
