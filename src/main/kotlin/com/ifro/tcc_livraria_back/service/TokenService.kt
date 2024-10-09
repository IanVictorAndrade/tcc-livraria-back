package com.ifro.tcc_livraria_back.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Usuario
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset


@Service
class TokenService(
    @Value(value = "\${api.security.token.secret}") private val secret: String
) {


    fun gerarToken(usuario: Usuario): String? {
        return try {

            val roles = usuario.role.map { it.nome }

            val token = JWT.create()
                .withIssuer("API Livraria Fábio")
                .withSubject(usuario.email)
                .withClaim("nome", usuario.nome)
                .withClaim("cpf", usuario.cpf)
                .withClaim("id", usuario.id)
                .withClaim("roles", roles)
                .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-04:00")))
                .sign(Algorithm.HMAC256(secret))

            token
        } catch (e: Exception) {
            throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, "erro ao gerar token jwt")
        }
    }

    fun getSubject(tokenJWT: String?): String {
        try {
            val algoritmo = Algorithm.HMAC256(secret)
            return JWT.require(algoritmo)
                .withIssuer("API Livraria Fábio")
                .build()
                .verify(tokenJWT)
                .subject
        } catch (e: Exception) {
            throw LivrariaException(HttpStatus.BAD_REQUEST, "Token JWT inválido ou expirado!")
        }
    }




}