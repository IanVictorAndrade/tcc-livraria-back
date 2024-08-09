package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.model.Imagem
import com.ifro.tcc_livraria_back.model.Livro
import com.ifro.tcc_livraria_back.repository.ImagemRepository
import com.ifro.tcc_livraria_back.repository.LivroRepository
import com.ifro.tcc_livraria_back.util.ImageUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImagemService(
    private val imagemRepository: ImagemRepository,
    private val livroRepository: LivroRepository,
) {
    val imageUtils = ImageUtils()

    fun salvarImagem(file: MultipartFile, livro: Livro): String? {
        val imagemSalva = imagemRepository.save(
            Imagem(
                nome = file.originalFilename!!,
                tipo = file.contentType!!,
                imagem = imageUtils.compressImage(file.bytes),
                livro = livro
            )
        )
        livro.imagem = imagemSalva
        livroRepository.save(livro)

        return "Imagem salva com sucesso! nome da imagem: " + imagemSalva.nome
    }

    fun buscarImagem(id: Long): ByteArray {
        val imagem = imagemRepository.findById(id)
        val imagens: ByteArray = imageUtils.decompressImage(imagem.get().imagem)
        return imagens
    }

    fun listarImagens(): List<Imagem> = imagemRepository.findAll()
}