package com.ifro.tcc_livraria_back.dto

import jakarta.validation.constraints.NotEmpty

data class DadosUsuario(
    @field:NotEmpty
    val id: Long,
    @field:NotEmpty
    val email: String,
    @field:NotEmpty
    val senha: String
)
