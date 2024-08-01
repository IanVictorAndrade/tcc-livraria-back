package com.ifro.tcc_livraria_back.dto

import jakarta.validation.constraints.NotBlank

data class DadosAutenticacao(
    @NotBlank
    val email: String,
    @NotBlank
    val senha: String
)
