package com.ifro.tcc_livraria_back.dto

data class UsuarioDTO (
    val email: String,
    val senha: String,
    val cpf: String,
    val nome: String,
    val role: RoleDTO?
)

data class RoleDTO(
    var nome: String
)

