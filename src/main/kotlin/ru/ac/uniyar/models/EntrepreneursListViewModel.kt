package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneur

class EntrepreneursListViewModel(val entrepreneurs: Iterable<Entrepreneur>) : ViewModel {

}
