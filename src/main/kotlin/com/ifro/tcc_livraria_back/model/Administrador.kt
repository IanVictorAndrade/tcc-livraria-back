package com.ifro.tcc_livraria_back.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Administrador (
    @Id
    val id: Long,
    val nome: String,
    val cpf: String,
    val email: String,
    val senha: String
)
