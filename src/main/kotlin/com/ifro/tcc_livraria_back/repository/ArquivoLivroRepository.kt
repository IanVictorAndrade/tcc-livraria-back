package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.ArquivoLivro
import org.springframework.data.jpa.repository.JpaRepository

interface ArquivoLivroRepository : JpaRepository<ArquivoLivro, Long> {
    fun findByGoogleDriveFileId(googleDriveFileId: String): ArquivoLivro?
}