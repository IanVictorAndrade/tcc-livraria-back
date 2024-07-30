package com.ifro.tcc_livraria_back.repository

import com.ifro.tcc_livraria_back.model.Livro
import org.springframework.data.jpa.repository.JpaRepository

interface LivroRepository : JpaRepository<Livro, Long> {
}