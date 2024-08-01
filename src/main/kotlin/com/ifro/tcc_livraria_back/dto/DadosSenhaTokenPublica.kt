package com.ifro.tcc_livraria_back.dto

import lombok.AllArgsConstructor
import lombok.Getter

@Getter
@AllArgsConstructor
data class DadosSenhaTokenPublica(
    val email: String,
    val dataCriacaoToken: Long
)
