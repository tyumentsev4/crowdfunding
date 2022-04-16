package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Entrepreneur

data class EntrepreneursListViewModel(val entrepreneurs: List<Entrepreneur>, val paginator: Paginator) : ViewModel
