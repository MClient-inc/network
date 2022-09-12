package ru.mclient.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NetworkAuthApplication

fun main(args: Array<String>) {
    runApplication<NetworkAuthApplication>(*args)
}