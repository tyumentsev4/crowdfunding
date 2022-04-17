package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Entrepreneur
import java.time.LocalDateTime

data class EntrepreneursListViewModel(
    val entrepreneurs: List<Entrepreneur>,
    val paginator: Paginator,
    val fromDateTime: LocalDateTime? = null,
    val toDateTime: LocalDateTime? = null,
    val nameSearch: String? = null
) : ViewModel
