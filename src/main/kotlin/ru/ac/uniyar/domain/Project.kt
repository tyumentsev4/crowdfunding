package ru.ac.uniyar.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Project(
    val id: Int,
    val addTime: LocalDateTime,
    val name: String,
    val entrepreneur: String,
    val description: String,
    val fundSize: Int,
    val fundraisingStart: LocalDateTime,
    val fundraisingEnd: LocalDateTime,
) {
    fun getProjectAddTime(): String {
        return addTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }

    fun getFundraisingStart(): String {
        return fundraisingStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }

    fun getFundraisingEnd(): String {
        return fundraisingEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}
