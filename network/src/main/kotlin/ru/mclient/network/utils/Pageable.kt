package ru.mclient.network.utils

import org.springframework.data.domain.Pageable


fun Int?.toPageable(): Pageable {
    return if (this == null) Pageable.unpaged() else Pageable.ofSize(this)
}