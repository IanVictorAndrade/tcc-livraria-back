package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.dto.DadosLivroDTO
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Imagem
import com.ifro.tcc_livraria_back.model.Livro
import com.ifro.tcc_livraria_back.repository.LivroRepository
import com.ifro.tcc_livraria_back.service.ImagemService
import com.ifro.tcc_livraria_back.service.LivroService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("/livro")
class LivroController(
    private val livroRepository: LivroRepository,
    private val livroService: LivroService,
    private val imagemService: ImagemService
) {

    @PostMapping("/cadastrar")
    fun cadastrarLivro(@RequestBody livro: Livro, uriComponentsBuilder: UriComponentsBuilder
    ) : ResponseEntity<String> {
        livroService.cadastrarLivro(livro)
        val uri = uriComponentsBuilder.path("/livro/{id}").buildAndExpand(livro.id).toUri()
        return ResponseEntity.created(uri).body("Livro cadastrado com sucesso!")
    }

    @GetMapping("/listar")
    fun listarLivros(): ResponseEntity<List<Livro?>?> = ResponseEntity.ok(livroService.listarLivros())

    @PutMapping("/editar/{id}")
    fun editarLivro(@RequestBody livro: DadosLivroDTO, @PathVariable id: Long) : ResponseEntity<String> {
        livroService.editarLivro(livro, id)
        return ResponseEntity.ok("Livro editado com sucesso!")
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarLivro(@PathVariable id: Long) : ResponseEntity<String> {
        livroService.deletarLivro(id)
        return ResponseEntity.ok("Livro deletado com sucesso!")
    }

    @PostMapping("/enviarImagem/{idLivro}")
    fun enviarImagem(@RequestParam("imagem") file: MultipartFile, @PathVariable idLivro: Long): ResponseEntity<String?> {
        val livro: Livro = livroRepository.findById(idLivro).orElseThrow { throw LivrariaException(HttpStatus.NOT_FOUND, "Livro não encontrado") }
        val imagemSalva = imagemService.salvarImagem(file, livro)
        return ResponseEntity.ok().body("$imagemSalva")
    }

    @GetMapping("/imagem/{id}")
    fun buscarImagem(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val imagemBuscada = imagemService.buscarImagem(id)
        val mediaType = MediaType.IMAGE_PNG // Supondo que todas as imagens sejam PNG

        return ResponseEntity.ok()
            .contentType(mediaType)
            .body(imagemBuscada)
    }

    @GetMapping("/listarImagens")
    fun listarImagens(): ResponseEntity<List<Imagem>> {
        val imagens: List<Imagem> = imagemService.listarImagens()
        return ResponseEntity.ok(imagens)
    }
}