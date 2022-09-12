package ru.mclient.network.branch

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.CONFLICT)
class CompanyAlreadyDisabled(): Exception()

@ResponseStatus(HttpStatus.CONFLICT)
class CompanyAlreadyExists(): Exception()

@ResponseStatus(HttpStatus.NOT_FOUND)
class CompanyNotExists: Exception()