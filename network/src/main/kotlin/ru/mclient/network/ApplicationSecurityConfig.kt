package ru.mclient.network

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
class ApplicationSecurityConfig {


    @Bean
    fun frameSecurityConfig(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeRequests()
            .mvcMatchers("/account/**").authenticated()
            .mvcMatchers("/companies/**").authenticated()
            .mvcMatchers("/networks/**").authenticated()
            .anyRequest().denyAll()
            .and()
            .oauth2ResourceServer()
            .jwt().and().and().build()
    }


}