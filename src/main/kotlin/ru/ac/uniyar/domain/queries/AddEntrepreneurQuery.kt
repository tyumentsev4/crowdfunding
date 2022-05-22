package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.EntrepreneursRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddEntrepreneurQuery(private val entrepreneursRepository: EntrepreneursRepository) {

    operator fun invoke(
        name: String
    ): UUID {
        return entrepreneursRepository.add(
            Entrepreneur(
                EMPTY_UUID,
                name,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            )
        )
    }
}
