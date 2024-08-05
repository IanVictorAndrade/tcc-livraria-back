package com.ifro.tcc_livraria_back.config.seguranca

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val securityFilter: SecurityFilter,
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun passwordEncoder() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {

        http.invoke {
            csrf { disable() }
            authorizeRequests {
                authorize("/usuario/listar", hasRole("ROLE_ADMIN"))
                authorize("/usuario/editar", hasRole("ROLE_ADMIN"))
                authorize("/usuario/deletar/{id}", hasRole("ROLE_ADMIN"))
                authorize("/h2-console/**", permitAll)
                authorize("/usuario/login", permitAll)
                authorize("/usuario/cadastro", permitAll)
                authorize("/usuario/codigo-senha", permitAll)
                authorize("/usuario/alterar-senha", permitAll)
                authorize("/{hash}", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/swagger-ui.html", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize(anyRequest, permitAll)
            }
            http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            headers { frameOptions { disable() } }
            httpBasic { }
        }
        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Autowired
    fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(BCryptPasswordEncoder())
    }

}