package ru.ac.uniyar.domain

import java.time.LocalDateTime


// Дата и время добавления, формируется приложением автоматически.
// Проект.
// ФИО инвестора, строка, может быть пустой.
// Контактная информация (телефон или email), строка, может быть пустой.
// Сумма, число.

data class Investment(
    val dateTime: LocalDateTime,
    val project: Project,
    val investorName: String,
    val contactInfo: String,
    val amount: Double
)
