package ru.ac.uniyar.domain.queries

class AmountShouldBePositiveInt : RuntimeException("Amount should be positive int")

class ProjectNotFound : RuntimeException("Project not found")

class StartTimeShouldBeLower : RuntimeException("Start date should be lower")

class FundSizeShouldBePositiveInt : RuntimeException("Fund size should be positive int")

class EntrepreneurNotFoundError : RuntimeException("Entrepreneur not found")

class EntrepreneurFetchError(message: String) : RuntimeException(message)

class InvestmentFetchError(message: String) : RuntimeException(message)
