package com.ifro.tcc_livraria_back.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "arquivo_livro")
data class ArquivoLivro (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val googleDriveFileId: String,
    val fileName: String,
    val contentType: String
)