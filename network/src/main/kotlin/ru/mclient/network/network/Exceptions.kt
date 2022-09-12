package ru.mclient.network.network

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.CONFLICT)
class CompanyNetworkDisabled(type: String, credential: String) :
    Exception("Company with $type=$credential disabled") {
    constructor(codename: String) : this("codename", codename)
    constructor(id: Long) : this("id", id.toString())
}

@ResponseStatus(HttpStatus.CONFLICT)
class CompanyNetworkAlreadyExists(type: String, credential: String) :
    Exception("Company with $type=$credential already exists") {
    constructor(codename: String) : this("codename", codename)
    constructor(id: Long) : this("id", id.toString())
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class CompanyNetworkNotExists(type: String, credential: String) :
    Exception("Company with $type=$credential does not exists") {
    constructor(codename: String) : this("codename", codename)
    constructor(id: Long) : this("id", id.toString())
}