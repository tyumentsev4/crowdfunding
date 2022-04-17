package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Investment
import java.time.LocalDateTime

data class InvestmentsListViewModel(
    val investments: Iterable<Investment>,
    val paginator: Paginator,
    val fromDateTime: LocalDateTime? = null,
    val toDateTime: LocalDateTime? = null,
    val fromAmount: Int? = null,
    val toAmount: Int? = null
) : ViewModel
