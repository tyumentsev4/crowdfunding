package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Investment

data class InvestmentsListViewModel(val investments: Iterable<Investment>, val paginator: Paginator) : ViewModel
