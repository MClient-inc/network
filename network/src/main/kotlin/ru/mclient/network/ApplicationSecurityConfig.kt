package ru.mclient.network

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain


@Configuration
class ApplicationSecurityConfig {


//    @Bean
//    fun frameSecurityConfig(http: HttpSecurity): SecurityFilterChain {
//        return http.headers()
//            .frameOptions().deny()
//            .and().build()
//    }
//

}