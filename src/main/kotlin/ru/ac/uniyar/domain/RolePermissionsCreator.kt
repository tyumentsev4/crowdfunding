package ru.ac.uniyar.domain

import ru.ac.uniyar.domain.storage.ENTREPRENEUR_ROLE_ID
import ru.ac.uniyar.domain.storage.REGISTERED_USER_ROLE_ID
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.Store
import java.nio.file.Path

val REGISTERED_USER_ROLE = RolePermissions(
    id = REGISTERED_USER_ROLE_ID,
    name = "Авторизованный пользователь",
    showStartPage = true,
    showEntrepreneursList = true,
    showEntrepreneur = true,
    showProjectsList = true,
    showProject = true,
    canRegister = true,
    openNewProject = true,
    addInvestment = true
)

val ENTREPRENEUR_ROLE = RolePermissions(
    id = ENTREPRENEUR_ROLE_ID,
    name = "Предприниматель",
    showStartPage = true,
    showEntrepreneursList = true,
    showEntrepreneur = true,
    showProjectsList = true,
    showProject = true,
    canRegister = true,
    openNewProject = true,
    addInvestment = true
)

fun main() {
    val store = Store(Path.of("storage.json"))
    val permissionsRepository = store.rolePermissionsRepository
    permissionsRepository.add(REGISTERED_USER_ROLE)
    permissionsRepository.add(ENTREPRENEUR_ROLE)
    store.save()
}
