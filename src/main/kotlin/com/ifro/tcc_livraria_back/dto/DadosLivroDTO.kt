package com.ifro.tcc_livraria_back.dto


data class DadosLivroDTO (
    val titulo: String,
    val autor: String,
    val descricao: String,
    val ano: Long,
    val preco: Double
)
