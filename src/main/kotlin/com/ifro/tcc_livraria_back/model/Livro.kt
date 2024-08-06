package com.ifro.tcc_livraria_back.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.util.Date

@Entity
data class Livro (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    val id: Long,
    val titulo: String,
    val autor: String,
    val ano: Date,
    val preco: Double,
    @OneToOne
    @JoinColumn(name = "id_arquivo_livro")
    @JsonIgnore
    @JsonManagedReference
    var arquivoLivro: ArquivoLivro? = null
)
