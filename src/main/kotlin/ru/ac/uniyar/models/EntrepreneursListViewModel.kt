package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneur

data class EntrepreneursListViewModel(val entrepreneurs: Iterable<Entrepreneur>, val paginator: Paginator) : ViewModel
