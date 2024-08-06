package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.dto.DadosLivroDTO
import com.ifro.tcc_livraria_back.model.Livro
import com.ifro.tcc_livraria_back.service.LivroService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/livro")
class LivroController(
    private val livroService: LivroService
) {

    @PostMapping("/cadastrar")
    fun cadastrarLivro(@RequestBody livro: Livro, uriComponentsBuilder: UriComponentsBuilder
    ) : ResponseEntity<String> {
        livroService.cadastrarLivro(livro)
        val uri = uriComponentsBuilder.path("/livro/{id}").buildAndExpand(livro.id).toUri()
        return ResponseEntity.created(uri).body("Usuário cadastrado com sucesso!")
    }

    @GetMapping("/listar")
    fun listarLivros(): ResponseEntity<List<Livro?>?> = ResponseEntity.ok(livroService.listarLivros())

    @PutMapping("/editar/{id}")
    fun editarLivro(@RequestBody livro: DadosLivroDTO, @PathVariable id: Long) {
        livroService.editarLivro(livro, id)
        ResponseEntity.ok("Livro editado com sucesso!")
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarLivro(@PathVariable id: Long) {
        livroService.deletarLivro(id)
        ResponseEntity.ok("Livro deletado com sucesso!")
    }
}