package com.ifro.tcc_livraria_back.dto

import jakarta.validation.constraints.NotBlank

data class AlterarSenhaRequest(
    @NotBlank
    val novaSenha: String,
    @NotBlank
    val token: String
)
