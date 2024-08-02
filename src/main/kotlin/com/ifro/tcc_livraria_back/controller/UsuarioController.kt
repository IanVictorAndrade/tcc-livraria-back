package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.dto.*
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Usuario
import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import com.ifro.tcc_livraria_back.service.TokenService
import com.ifro.tcc_livraria_back.service.UserPasswordService
import com.ifro.tcc_livraria_back.service.UsuarioService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
    private val userPasswordService: UserPasswordService,
    private val tokenService: TokenService
) {

    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @PostMapping("/cadastro")
    @Transactional
    fun cadastrarUsuario(@RequestBody dados: UsuarioDTO, uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any> {
        usuarioService.cadastrar(dados)
        val uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(dados.id).toUri()
        return ResponseEntity.created(uri).body("Usuário cadastrado com sucesso!")
    }

    @GetMapping("/listar")
    @SecurityRequirement(name = "bearer-key")
    fun listarUsuarios(): ResponseEntity<List<Usuario?>> {
        val lista = usuarioService.listar()
        return ResponseEntity.ok(lista)
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    fun buscarPorId(@PathVariable id: Long): Optional<Usuario> {
        return usuarioService.buscarPorId(id)
    }

    @PutMapping("/editar")
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    fun editarUsuario(@RequestBody user: Usuario): ResponseEntity<String> {
        usuarioService.edita(user)
        return ResponseEntity.ok("Usuário Editado com sucesso!")
    }

    @DeleteMapping("deletar/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    fun deletaUsuario(@PathVariable id: Long): ResponseEntity<HttpStatus> {
        usuarioService.deletar(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid dados: DadosAutenticacao): ResponseEntity<Any> {
        val token = UsernamePasswordAuthenticationToken(dados.email, dados.senha)
        val autenticador = authenticationManager?.authenticate(token)
        val tokenJWT: String = tokenService.gerarToken(autenticador?.principal as Usuario) ?: throw LivrariaException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar token")
        return ResponseEntity.ok(DadosTokenJwt(tokenJWT))
    }

    @PostMapping("/codigo-senha")
    @Transactional
    fun gerarTokenRecuperacaoSenha(@RequestBody email: EmailRequest): ResponseEntity<Any> {
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