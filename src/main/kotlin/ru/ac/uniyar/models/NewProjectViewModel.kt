package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Entrepreneur

data class NewProjectViewModel(val form: WebForm, val entrepreneurs: List<Entrepreneur>) : ViewModel
