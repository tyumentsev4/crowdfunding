package ru.ac.uniyar.domain

import ru.ac.uniyar.domain.queries.AddUserQuery
import ru.ac.uniyar.domain.queries.AddInvestmentQuery
import ru.ac.uniyar.domain.queries.AddProjectQuery
import ru.ac.uniyar.domain.queries.AuthenticateUserViaLoginQuery
import ru.ac.uniyar.domain.queries.CloseProjectQuery
import ru.ac.uniyar.domain.queries.DeleteProjectQuery
import ru.ac.uniyar.domain.queries.EditProjectQuery
import ru.ac.uniyar.domain.queries.FetchEntrepreneurQuery
import ru.ac.uniyar.domain.queries.FetchInvestmentQuery
import ru.ac.uniyar.domain.queries.FetchPermissionsViaIdQuery
import ru.ac.uniyar.domain.queries.FetchProjectQuery
import ru.ac.uniyar.domain.queries.FetchProjectViaIdQuery
import ru.ac.uniyar.domain.queries.FetchUserQuery
import ru.ac.uniyar.domain.queries.FetchUserViaUserId
import ru.ac.uniyar.domain.queries.InvestmentsByProjectQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursPerPageQuery
import ru.ac.uniyar.domain.queries.ListEntrepreneursQuery
import ru.ac.uniyar.domain.queries.ListInvestmentsPerPageQuery
import ru.ac.uniyar.domain.queries.ListInvestmentsQuery
import ru.ac.uniyar.domain.queries.ListOpenProjectsQuery
import ru.ac.uniyar.domain.queries.ListProjectsPerPageQuery
import ru.ac.uniyar.domain.queries.ListUserProjectsPerPageQuery
import ru.ac.uniyar.domain.storage.Settings
import ru.ac.uniyar.domain.storage.Store
import java.nio.file.Path

class StoreInitializer(
    documentStorePath: Path,
    settingsPath: Path
) {
    private val store = Store(documentStorePath)
    val settings = Settings(settingsPath)

    val listProjectsPerPageQuery = ListProjectsPerPageQuery(store.projectsRepository)
    val listEntrepreneursPerPageQuery = ListEntrepreneursPerPageQuery(store.usersRepository)
    val fetchEntrepreneurQuery = FetchEntrepreneurQuery(store.usersRepository, store.projectsRepository)
    val addUserQuery = AddUserQuery(store.usersRepository, settings)
    val fetchProjectQuery =
        FetchProjectQuery(store.projectsRepository, store.usersRepository, store.investmentsRepository)
    val listEntrepreneursQuery = ListEntrepreneursQuery(store.usersRepository)
    val addProjectQuery = AddProjectQuery(store.projectsRepository, store.usersRepository)
    val listInvestmentsPerPageQuery = ListInvestmentsPerPageQuery(store.investmentsRepository)
    val listOpenProjectQuery = ListOpenProjectsQuery(store.projectsRepository)
    val addInvestmentQuery = AddInvestmentQuery(store.investmentsRepository, store.projectsRepository)
    val fetchInvestmentQuery = FetchInvestmentQuery(store.investmentsRepository, store.projectsRepository)
    val fetchPermissionsViaIdQuery = FetchPermissionsViaIdQuery(store.rolePermissionsRepository)
    val fetchUserViaUserId = FetchUserViaUserId(store.usersRepository)
    val authenticateUserViaLoginQuery = AuthenticateUserViaLoginQuery(store.usersRepository, settings)
    val fetchProjectViaIdQuery = FetchProjectViaIdQuery(store.projectsRepository)
    val fetchUserQuery = FetchUserQuery(store.usersRepository, store.investmentsRepository, store.projectsRepository)
    val listUserProjectsPerPageQuery = ListUserProjectsPerPageQuery(store.projectsRepository, store.investmentsRepository)
    val editProjectQuery = EditProjectQuery(store.projectsRepository)
    val investmentsByProjectQuery = InvestmentsByProjectQuery(store.investmentsRepository, store.projectsRepository)
    val deleteProjectQuery = DeleteProjectQuery(store.projectsRepository)
    val listInvestmentsQuery = ListInvestmentsQuery(store.investmentsRepository)
    val closeProjectQuery = CloseProjectQuery(store.projectsRepository)
}