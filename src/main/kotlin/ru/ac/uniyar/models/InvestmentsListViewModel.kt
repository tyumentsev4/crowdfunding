package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Investment
import java.time.LocalDateTime

data class InvestmentsListViewModel(
    val investments: Iterable<Investment>,
    val paginator: Paginator,
    val fromDateTime: LocalDateTime? = null,
    val toDateTime: LocalDateTime? = null,
    val fromAmount: Double? = null,
    val toAmount: Double? = null
) : ViewModel
