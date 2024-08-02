package com.ifro.tcc_livraria_back.repository


import com.ifro.tcc_livraria_back.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails

interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun findByEmail(email: String?): Usuario?

    fun findUsuarioByEmail(email: String?): UserDetails?

}