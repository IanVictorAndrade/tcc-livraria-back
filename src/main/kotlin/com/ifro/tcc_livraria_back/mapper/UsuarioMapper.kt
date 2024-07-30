package com.ifro.tcc_livraria_back.mapper

import com.ifro.tcc_livraria_back.dto.DadosUsuario
import com.ifro.tcc_livraria_back.model.Usuario
import org.springframework.stereotype.Component

@Component
class UsuarioMapper: Mapper<Usuario, DadosUsuario> {


    override fun map(t: Usuario): DadosUsuario {
        return DadosUsuario(
            id = t.id,
            email = t.email,
            senha = t.senha
        )
    }
}