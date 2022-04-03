package ru.ac.uniyar.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class Project(
    val projectAddTime: LocalDateTime,
    val name: String,
    val entrepreneur: String,
    val description: String,
    val fundSize: Int,
    val fundraisingStart: LocalDateTime,
    val fundraisingEnd: LocalDateTime,
) {
    fun getProjectAddTime(): String {
        return projectAddTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }

    fun getFundraisingStart(): String {
        return fundraisingStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }

    fun getFundraisingEnd(): String {
        return fundraisingEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}
