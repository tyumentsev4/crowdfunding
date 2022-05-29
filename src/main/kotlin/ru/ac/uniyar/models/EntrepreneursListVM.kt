package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.User
import java.time.LocalDateTime

data class EntrepreneursListVM(
    val entrepreneurs: List<User>,
    val paginator: Paginator,
    val fromDateTime: LocalDateTime? = null,
    val toDateTime: LocalDateTime? = null,
    val nameSearch: String? = null
) : ViewModel
