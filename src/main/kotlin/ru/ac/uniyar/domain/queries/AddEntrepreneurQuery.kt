package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Entrepreneur
import ru.ac.uniyar.domain.storage.Store
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class AddEntrepreneurQuery(store: Store) {
    private val entrepreneursRepository = store.entrepreneursRepository

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
