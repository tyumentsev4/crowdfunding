package ru.ac.uniyar.domain.queries

class AmountShouldBePositiveInt : RuntimeException("Amount should be positive int")

class ProjectNotFound : RuntimeException("Project not found")

class StartTimeShouldBeLower : RuntimeException("Start date should be lower")

class FundSizeShouldBePositiveInt : RuntimeException("Fund size should be positive int")

class EntrepreneurFetchError(message: String) : RuntimeException(message)

class InvestmentFetchError(message: String) : RuntimeException(message)

class ProjectFetchError(message: String) : RuntimeException(message)

class UserFetchError(message: String) : RuntimeException(message)
