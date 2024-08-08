package com.ifro.tcc_livraria_back.dto

import java.util.Date

data class DadosLivroDTO (
    val titulo: String,
    val autor: String,
    val ano: Long,
    val preco: Double
)
