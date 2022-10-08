package ru.mclient.auth.controller

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import javax.swing.text.html.HTML
import javax.swing.text.html.HTMLDocument


class RegisterInput(
    val username: String,
    val password: String,
)

@RestController
class AuthController(
    private val userDetailsManager: UserDetailsManager,
    private val passwordEncoder: PasswordEncoder,
) {


    @PostMapping("/register", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun register(input: RegisterInput) {
        userDetailsManager.createUser(
            User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(input.username)
                .password(input.password)
                .authorities("ROLE_user")
                .build()
        )
    }

}