package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.dto.DadosSenhaTokenPublica
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Usuario
import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.token.KeyBasedPersistenceTokenService
import org.springframework.security.core.token.SecureRandomFactoryBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class UserPasswordService(
    val usuarioRepository: UsuarioRepository,
    val passwordEncoder: BCryptPasswordEncoder
) {

    fun gerandoToken(usuario: Usuario) : String {
        val tokenService = criarInstanciaPara(usuario)

        val token = tokenService.allocateToken(usuario.email)


        return token.key
    }

    private fun criarInstanciaPara(usuario: Usuario): KeyBasedPersistenceTokenService {
        val tokenService = KeyBasedPersistenceTokenService()
        tokenService.setServerSecret(usuario.password)
        tokenService.setServerInteger(16)
        tokenService.setSecureRandom(SecureRandomFactoryBean().`object`)
        return tokenService
    }

    fun trocarSenha(novaSenha: String, rawToken: String) : ResponseEntity<String> {

        val dadosPublic: DadosSenhaTokenPublica = leitorDadosPublicos(rawToken)

        if (isExpired(dadosPublic)) {
            throw LivrariaException(HttpStatus.BAD_REQUEST, "Token expirado")
        }

        val usuario = usuarioRepository.findByEmail(dadosPublic.email) ?: throw LivrariaException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        val tokenService = this.criarInstanciaPara(usuario)
        try {
            tokenService.verifyToken(rawToken)
        } catch (e: Exception) {
            throw LivrariaException(HttpStatus.BAD_REQUEST, "Token inválido")
        }

        usuario.senha = passwordEncoder.encode(novaSenha)
        usuarioRepository.save(usuario)

        return ResponseEntity.ok().body("Senha alterada com sucesso")
    }

    private fun isExpired(dadosPublic: DadosSenhaTokenPublica): Boolean {
        val criadoEm = Date(dadosPublic.dataCriacaoToken)
        val cincoMinutosDepois = Date(criadoEm.time + 5 * 60 * 1000)
        val agora = Date()
        return cincoMinutosDepois.before(agora)
    }


    private fun leitorDadosPublicos(rawToken: String): DadosSenhaTokenPublica {
        val token = String(Base64.getDecoder().decode(rawToken))
        val tokenParts = token.split(":")
        val timestamp = tokenParts[0].toLong()
        val email = tokenParts[2]
        return DadosSenhaTokenPublica(email, timestamp)
    }
}