package com.ifro.tcc_livraria_back.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.Date

@Entity
data class Livro (
    @Id
    val id: Long,
    val titulo: String,
    val autor: String,
    val ano: Date,
    val preco: Double,
)
