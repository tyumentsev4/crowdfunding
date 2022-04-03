package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Project

class NewInvestmentViewModel(webForm: WebForm, val projects: Iterable<Project>): ViewModel
