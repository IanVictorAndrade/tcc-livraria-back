package com.ifro.tcc_livraria_back.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor


@Entity
@Table(name = "imagem")
@Data
@AllArgsConstructor
@NoArgsConstructor

data class Imagem (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagem")
    val id: Long = 0,
    val nome: String,
    val tipo: String,
    @Lob
    @Column(name = "imagem")
    val imagem: ByteArray,
    @OneToOne(mappedBy = "imagem", fetch = FetchType.EAGER)
    val livro: Livro
)