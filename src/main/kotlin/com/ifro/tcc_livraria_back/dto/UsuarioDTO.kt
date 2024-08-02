package com.ifro.tcc_livraria_back.dto

data class UsuarioDTO (
    val id: Long,
    val email: String,
    val senha: String,
    val cpf: String,
    val nome: String,
    val role: String
)
