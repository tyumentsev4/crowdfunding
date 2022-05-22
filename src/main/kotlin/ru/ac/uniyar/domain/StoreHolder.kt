package ru.ac.uniyar.domain

import ru.ac.uniyar.domain.queries.AddEntrepreneurQuery
import ru.ac.uniyar.domain.queries.AddInvestmentQuery
import ru.ac.uniyar.domain.queries.AddProjectQuery
import ru.ac.uniyar.domain.queries.FetchEntrepreneurQuery
import ru.ac.uniyar.domain.queries.FetchInvestmentQuery
import ru.ac.uniyar.domain.queries.FetchProjectQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursPerPageQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursQuery
import ru.ac.uniyar.domain.queries.ListInvestmentsPerPageQuery
import ru.ac.uniyar.domain.queries.ListOpenProjectsQuery
import ru.ac.uniyar.domain.queries.ListProjectsPerPageQuery
import ru.ac.uniyar.domain.storage.Settings
import ru.ac.uniyar.domain.storage.Store
import java.nio.file.Path

class StoreHolder(
    documentStorePath: Path,
    settingsPath: Path
) {
    val store = Store(documentStorePath)
    val settings = Settings(settingsPath)

    val listProjectsPerPageQuery = ListProjectsPerPageQuery(store.projectsRepository)
    val listEntrepreneursPerPageQuery = ListEntrepreneursPerPageQuery(store.entrepreneursRepository)
    val fetchEntrepreneurQuery = FetchEntrepreneurQuery(store.entrepreneursRepository, store.projectsRepository)
    val addEntrepreneurQuery = AddEntrepreneurQuery(store.entrepreneursRepository)
    val fetchProjectQuery =
        FetchProjectQuery(store.projectsRepository, store.entrepreneursRepository, store.investmentsRepository)
    val listEntrepreneursQuery = ListEntrepreneursQuery(store.entrepreneursRepository)
    val addProjectQuery = AddProjectQuery(store.projectsRepository, store.entrepreneursRepository)
    val listInvestmentsPerPageQuery = ListInvestmentsPerPageQuery(store.investmentsRepository)
    val listOpenProjectQuery = ListOpenProjectsQuery(store.projectsRepository)
    val addInvestmentQuery = AddInvestmentQuery(store.investmentsRepository, store.projectsRepository)
    val fetchInvestmentQuery = FetchInvestmentQuery(store.investmentsRepository, store.projectsRepository)
}