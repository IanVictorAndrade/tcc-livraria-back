package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long> {
}