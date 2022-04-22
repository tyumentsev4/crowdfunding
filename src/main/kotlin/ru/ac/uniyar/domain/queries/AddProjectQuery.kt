package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Project
import ru.ac.uniyar.domain.storage.Store
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class AddProjectQuery(store: Store) {
    private val projectsRepository = store.projectsRepository

    @Suppress("LongParameterList")
    operator fun invoke(
        name: String,
        entrepreneurId: UUID,
        description: String,
        fundSize: Int,
        fundraisingStart: LocalDateTime,
        fundraisingEnd: LocalDateTime,
    ): UUID {
        if (fundraisingEnd <= fundraisingStart)
            throw StartTimeShouldBeLower()
        if (fundSize <= 0)
            throw FundSizeShouldBePositiveInt()
        return projectsRepository.add(
            Project(
                EMPTY_UUID,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                name,
                entrepreneurId,
                description,
                fundSize,
                fundraisingStart,
                fundraisingEnd
            )
        )
    }
}

class StartTimeShouldBeLower : RuntimeException("Start date should be lower")

class FundSizeShouldBePositiveInt : RuntimeException("Fund size should be positive int")
