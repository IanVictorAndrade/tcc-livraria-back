package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.Imagem
import org.springframework.data.jpa.repository.JpaRepository

interface ImagemRepository : JpaRepository<Imagem, Long> {
}