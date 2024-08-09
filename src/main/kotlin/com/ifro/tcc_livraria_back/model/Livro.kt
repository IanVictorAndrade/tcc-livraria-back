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
    val id: Long = 0,
    var titulo: String,
    var autor: String,
    var ano: Long,
    var preco: Double,
    @OneToOne
    @JoinColumn(name = "id_imagem")
    @JsonManagedReference
    @JsonIgnore
    var imagem: Imagem? = null,
    @OneToOne
    @JoinColumn(name = "id_arquivo_livro")
    @JsonManagedReference
    @JsonIgnore
    var arquivoLivro: ArquivoLivro? = null
)
