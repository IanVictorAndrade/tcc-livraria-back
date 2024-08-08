package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.dto.UsuarioDTO
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Role
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

        val role = roleRepository.findByNome(user.role!!.nome) ?: throw LivrariaException(HttpStatus.BAD_REQUEST, "Role não encontrada")

        val existeUsuarioPorEmail = usuarioRepository.findByEmail(user.email) != null
        val existeUsuarioPorCpf = usuarioRepository.findByCpf(user.cpf) != null

        if (existeUsuarioPorEmail || existeUsuarioPorCpf) throw LivrariaException(HttpStatus.BAD_REQUEST, "E-mail ou CPF já cadastrado")

        val usuario = Usuario(
            nome = user.nome,
            cpf = user.cpf,
            email = user.email,
            senha = user.senha,
            role = mutableSetOf(role)
        )

        usuarioRepository.save(usuario)
    }

    fun listar(): List<Usuario?>? = usuarioRepository.findAll()

    fun edita(id: Long, user: UsuarioDTO) {
        val usuarioDB = usuarioRepository.findById(id).orElseThrow { LivrariaException(HttpStatus.NOT_FOUND, "usuário não encontrado") }
        usuarioDB.email = user.email
        usuarioDB.senha = passwordEncoder.encode(user.senha)
        usuarioDB.cpf = user.cpf
        usuarioDB.nome = user.nome
        // Atualiza o role do usuário
        if (user.role != null) {
            val role = roleRepository.findByNome(user.role.nome) ?: throw LivrariaException(HttpStatus.BAD_REQUEST, "Role não encontrada")
                usuarioDB.role = mutableSetOf(role)
        }
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