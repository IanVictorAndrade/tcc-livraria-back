package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.dto.UsuarioDTO
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Usuario
import com.ifro.tcc_livraria_back.repository.RoleRepository
import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val roleRepository: RoleRepository,
    private val javaMailSender: JavaMailSender
) {

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Value("\${spring.mail.username}")
    private val sender: String? = null

    fun cadastrar(user: UsuarioDTO) {

        val usuario = Usuario(0, "", "", "", "")
        val role = roleRepository.findByNome(user.role)
            ?: throw LivrariaException(HttpStatus.BAD_REQUEST, "Role não encontrada")


        if (user.id != 0L) {
            throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, "campo id tem que ser 0")
        }


        usuario.id = 0L
        usuario.email = user.email
        usuario.senha = passwordEncoder.encode(user.senha)
        usuario.cpf = user.cpf
        usuario.nome = user.nome
        usuario.roles = setOf(role)


        val existeUsuario = usuarioRepository.findByEmail(user.email)
        if (existeUsuario == null) {
            usuarioRepository.save(usuario)
        }
    }

    fun listar(): List<Usuario?>? = usuarioRepository.findAll()

    fun edita(user: Usuario) {
        val usuarioDB = usuarioRepository.findById(user.id).orElseThrow { LivrariaException(HttpStatus.NOT_FOUND, "usuário não encontrado") }
        usuarioDB.email = user.email
        usuarioDB.senha = passwordEncoder.encode(user.senha)
        usuarioDB.cpf = user.cpf
        usuarioDB.nome = user.nome
        usuarioRepository.save(usuarioDB)
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