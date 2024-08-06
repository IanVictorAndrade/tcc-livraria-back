package com.ifro.tcc_livraria_back.dto

import com.ifro.tcc_livraria_back.model.Role

data class UsuarioDTO (
    val email: String,
    val senha: String,
    val cpf: String,
    val nome: String,
    val role: Role?
)
