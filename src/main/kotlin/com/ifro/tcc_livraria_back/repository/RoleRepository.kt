package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.Role
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {
    fun findByNome(nome: String): Role?
}