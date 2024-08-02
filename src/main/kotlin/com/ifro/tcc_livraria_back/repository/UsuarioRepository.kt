package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.security.core.userdetails.UserDetails
import java.util.Optional

interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun findByEmail(email: String?): Usuario?

    fun findUsuarioByEmail(email: String?): UserDetails?

}