package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Project

data class NewInvestmentVM(val form: WebForm, val project: Project) : ViewModel
