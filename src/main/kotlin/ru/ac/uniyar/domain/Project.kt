package ru.ac.uniyar.domain

import java.time.LocalDateTime

data class Project(
    val name: String,
    val entrepreneur: String,
    val description: String,
    val fundSize: Int,
    val fundraisingStart: LocalDateTime,
    val fundraisingEnd: LocalDateTime
)