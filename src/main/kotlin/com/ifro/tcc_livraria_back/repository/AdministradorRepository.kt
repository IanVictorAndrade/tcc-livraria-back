package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.Administrador
import org.springframework.data.jpa.repository.JpaRepository

interface AdministradorRepository : JpaRepository<Administrador, Long> {
}