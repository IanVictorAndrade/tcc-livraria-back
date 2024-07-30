package com.ifro.tcc_livraria_back.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ifro.tcc_livraria_back.dto.DadosUsuario
import com.ifro.tcc_livraria_back.mapper.UsuarioMapper
import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val javaMailSender: JavaMailSender
) {

    @Value("\${spring.mail.username}")
    private val sender: String? = null


    fun listar(): List<DadosUsuario> = usuarioRepository.findAll().stream().map { t -> usuarioMapper.map(t) }.collect(Collectors.toList())

    fun edita(user: DadosUsuario) {
        val usuarioDB = usuarioRepository.findById(user.id).orElseThrow { NotFoundException() }
        usuarioDB.email = user.email
        val senhaCriptografada = passwordEncoder.encode(user.senha)
        usuarioDB.senha = senhaCriptografada
    }

    fun enviandoEmailDeRecuperacao(email: String): String {
        val emailConvertido = ObjectMapper().readTree(email)["email"].asText()

        val usuario = usuarioRepository.findByEmail(emailConvertido)

        if (usuario != null) {
            val token = UUID.randomUUID().toString()
//            usuario.token = token
            usuarioRepository.save(usuario)


            return try {
                val mailMessage = SimpleMailMessage()

                mailMessage.from = sender
                mailMessage.setTo(emailConvertido)
                mailMessage.subject = "Recuperação de Senha"
                mailMessage.text = "Seu token de recuperação é: $token"

                javaMailSender.send(mailMessage)
                val jsonResponse = ObjectMapper().writeValueAsString("E-mail enviado com Sucesso!")
                jsonResponse

            } catch (e: Exception) {
                "Erro ao enviar o e-mail"
            }
        } else {
            throw RuntimeException("Usuário não existe")
        }


    }
}