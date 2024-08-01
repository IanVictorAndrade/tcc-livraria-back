package com.ifro.tcc_livraria_back.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ifro.tcc_livraria_back.dto.DadosUsuario
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.mapper.UsuarioMapper
import com.ifro.tcc_livraria_back.model.Usuario
import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    fun cadastrar(dados: DadosUsuario) {

        val usuario = Usuario(0, "", "", "", "")


        if (dados.id != 0L) {
            throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, "campo id tem que ser 0")
        }


        usuario.email = dados.email
        val senhaCriptografada = passwordEncoder.encode(dados.senha)
        usuario.senha = senhaCriptografada


        val existeUsuario = usuarioRepository.findByEmail(dados.email)
        if (existeUsuario == null) {
            usuarioRepository.save(usuario)
        }
    }

    fun listar(): List<DadosUsuario> = usuarioRepository.findAll().stream().map { t -> usuarioMapper.map(t) }.collect(Collectors.toList())

    fun edita(user: DadosUsuario) {
        val usuarioDB = usuarioRepository.findById(user.id).orElseThrow { LivrariaException(HttpStatus.NOT_FOUND, "usuário não encontrado") }
        usuarioDB.email = user.email
        val senhaCriptografada = passwordEncoder.encode(user.senha)
        usuarioDB.senha = senhaCriptografada
    }

    fun deletar(id: Long) = usuarioRepository.deleteById(id)

    fun buscarPorId(id: Long): Optional<Usuario> = usuarioRepository.findById(id)

    fun enviandoEmailDeRecuperacao(email: String, token: String): ResponseEntity<Any> {
            return try {
                val mailMessage = SimpleMailMessage()

                mailMessage.from = sender
                mailMessage.setTo(email)
                mailMessage.subject = "Recuperação de Senha"
                mailMessage.text = "Seu token de recuperação é: $token"

                javaMailSender.send(mailMessage)
                ResponseEntity.ok("E-mail enviado com sucesso!")

            } catch (e: Exception) {
                throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar e-mail")
            }
    }
}