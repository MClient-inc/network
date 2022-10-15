package ru.mclient.auth

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.sql.DataSource
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


@Configuration
class SecurityConfig {
    @Bean
    @Order(1)
    @Throws(Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.exceptionHandling { exceptions: ExceptionHandlingConfigurer<HttpSecurity?> ->
            exceptions
                .authenticationEntryPoint(
                    LoginUrlAuthenticationEntryPoint("/login")
                )
        }
        return http.build()
    }

    @Bean
    @Order(2)
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeRequests()
            .mvcMatchers("/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .cors().disable()
            .sessionManagement().disable()
            .formLogin()
            .and()
            .build()
    }


    @Bean
    fun userDetailsService(dataSource: DataSource): UserDetailsManager {
//        val userDetails = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build()w
        return JdbcUserDetailsManager(dataSource)
//        return InMemoryUserDetailsManager(userDetails)
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient: RegisteredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .redirectUri("ru.mclient.oauth://mclient.ru/callback")
            .scope(OidcScopes.OPENID)
            .scope("message.read")
            .scope("message.write")
            .build()
        return InMemoryRegisteredClientRepository(registeredClient)
    }


    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyPair = generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        val rsaKey: RSAKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
        val jwkSet = JWKSet(rsaKey)
        return ImmutableJWKSet<SecurityContext>(jwkSet)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder()
            .build()
    }

    companion object {
        private fun generateRsaKey(): KeyPair {
            val keyPair: KeyPair = try {
                val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
                keyPairGenerator.initialize(2048)
                keyPairGenerator.generateKeyPair()
            } catch (ex: Exception) {
                throw IllegalStateException(ex)
            }
            return keyPair
        }
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}