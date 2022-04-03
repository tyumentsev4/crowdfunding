package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneur

data class NewProjectViewModel(val form: WebForm, val entrepreneurs: Iterable<Entrepreneur>) : ViewModel
