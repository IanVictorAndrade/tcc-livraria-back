package com.ifro.tcc_livraria_back.config.seguranca

import com.ifro.tcc_livraria_back.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AutenticacaoService(
    val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        return usuarioRepository.findUsuarioByEmail(username)
    }
}