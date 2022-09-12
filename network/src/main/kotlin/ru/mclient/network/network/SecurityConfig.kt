package ru.mclient.network.network

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
class ChainSecurityConfig {

    @Bean
    fun configureChainSecurityConfig(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .authorizeRequests()
            .mvcMatchers("/chains/**").permitAll()
            .and()
            .build()
    }

}