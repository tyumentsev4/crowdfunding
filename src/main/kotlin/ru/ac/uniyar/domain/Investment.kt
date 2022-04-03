package ru.ac.uniyar.domain

import java.time.LocalDateTime

data class Investment(
    val id: Int,
    val dateTime: LocalDateTime,
    val project: String,
    val investorName: String,
    val contactInfo: String,
    val amount: Double
)
