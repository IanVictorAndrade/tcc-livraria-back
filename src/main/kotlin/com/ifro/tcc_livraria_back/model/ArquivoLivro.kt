package com.ifro.tcc_livraria_back.model

import jakarta.persistence.*

@Entity(name = "arquivo_livro")
data class ArquivoLivro (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arquivo_livro")
    val id: Long = 0,
    val googleDriveFileId: String,
    val fileName: String,
    val contentType: String,
    @OneToOne(mappedBy = "arquivoLivro", fetch = FetchType.EAGER)
    val livro: Livro?
)