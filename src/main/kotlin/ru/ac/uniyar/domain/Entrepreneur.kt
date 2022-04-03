package ru.ac.uniyar.domain

import java.time.LocalDateTime

data class Entrepreneur(
    val id: Int,
    val name: String,
    val addTime: LocalDateTime
)
