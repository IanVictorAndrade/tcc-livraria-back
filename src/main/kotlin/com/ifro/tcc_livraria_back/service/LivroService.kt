package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.dto.DadosLivroDTO
import com.ifro.tcc_livraria_back.exception.LivrariaException
import com.ifro.tcc_livraria_back.model.Livro
import com.ifro.tcc_livraria_back.repository.ImagemRepository
import com.ifro.tcc_livraria_back.repository.LivroRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class LivroService(
    private val livroRepository: LivroRepository,
    private val imagemRepository: ImagemRepository
) {
    fun cadastrarLivro(livro: Livro) {
        val existeLivro = livroRepository.existsById(livro.id)
        if (existeLivro) throw LivrariaException(HttpStatus.BAD_REQUEST, "esse livro já existe")

        val livroDB = Livro(
            titulo = livro.titulo,
            autor = livro.autor,
            descricao = livro.descricao,
            ano = livro.ano,
            preco = livro.preco
        )
        livroRepository.save(livroDB)
    }

    fun listarLivros(): List<Livro> = livroRepository.findAll()

    fun editarLivro(livro: DadosLivroDTO, id: Long) {
        val livroDB = livroRepository.findById(id).orElseThrow { LivrariaException(HttpStatus.NOT_FOUND, "livro não encontrado") }
        livroDB.preco = livro.preco
        livroDB.ano = livro.ano
        livroDB.descricao = livro.descricao
        livroDB.titulo = livro.titulo
        livroDB.autor = livro.autor
        livroRepository.save(livroDB)
    }

    fun deletarLivro(id: Long) {
        livroRepository.deleteById(id)
    }
}