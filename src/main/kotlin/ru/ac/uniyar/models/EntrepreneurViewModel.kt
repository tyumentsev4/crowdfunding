package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entrepreneur

data class EntrepreneurViewModel(val entrepreneur: Entrepreneur) : ViewModel
