package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.dto.AlterarSenhaRequest
import com.ifro.tcc_livraria_back.dto.DadosUsuario
import com.ifro.tcc_livraria_back.dto.EmailRequest
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Usuario
import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import com.ifro.tcc_livraria_back.service.UserPasswordService
import com.ifro.tcc_livraria_back.service.UsuarioService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
class UsuarioController(
    private val usuarioService: UsuarioService,
    private val usuarioRepository: UsuarioRepository,
    private val userPasswordService: UserPasswordService
) {

    @PostMapping("/cadastro")
    @Transactional
    fun cadastrarUsuario(@RequestBody dados: DadosUsuario, uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any> {
        usuarioService.cadastrar(dados)
        val uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(dados.id).toUri()
        return ResponseEntity.created(uri).body("Usuário cadastrado com sucesso!")
    }

    @GetMapping("/listar")
    @SecurityRequirement(name = "bearer-key")
    fun listarUsuarios(): ResponseEntity<List<DadosUsuario>> {
        val lista = usuarioService.listar()
        return ResponseEntity.ok(lista)
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    fun buscarPorId(@PathVariable id: Long): Optional<Usuario> {
        return usuarioService.buscarPorId(id)
    }

    @PutMapping
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    fun editarUsuario(@RequestBody @Valid user: DadosUsuario): ResponseEntity<String> {
        usuarioService.edita(user)
        return ResponseEntity.ok("Usuário Editado com sucesso!")
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    fun deletaUsuario(@PathVariable id: Long): ResponseEntity<HttpStatus> {
        usuarioService.deletar(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/codigo-senha")
    @Transactional
    fun gerarToken(@RequestBody email: EmailRequest): ResponseEntity<Any> {
        val usuario: Usuario = usuarioRepository.findByEmail(email.email) ?: throw LivrariaException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        val token = userPasswordService.gerandoToken(usuario)
        return usuarioService.enviandoEmailDeRecuperacao(email.email, token)
    }

    @PutMapping("/alterar-senha")
    @Transactional
    fun alterarSenha(@RequestBody request: AlterarSenhaRequest): ResponseEntity<String> {
        return userPasswordService.trocarSenha(request.novaSenha, request.token)
    }
}