package ru.mclient.network

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@EnableJpaAuditing
@SpringBootApplication
class NetworkApplication

fun main(args: Array<String>) {
    runApplication<NetworkApplication>(*args)
}