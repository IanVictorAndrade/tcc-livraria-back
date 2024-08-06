package com.ifro.tcc_livraria_back.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity(name = "Usuario")
@Table(name = "usuarios",
       uniqueConstraints = [
           UniqueConstraint(columnNames = ["email"]),
           UniqueConstraint(columnNames = ["cpf"])
       ])
data class Usuario (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @NotBlank
    var nome: String,
    @NotBlank
    var cpf: String,
    @NotBlank
    var email: String,
    @NotBlank
    var senha: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = [JoinColumn(name = "usuario_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var role: MutableSet<Role> = mutableSetOf()
) : UserDetails {
    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return role.map { SimpleGrantedAuthority(it.nome) }
    }

    @JsonIgnore
    override fun getPassword(): String {
        return senha
    }

    @JsonIgnore
    override fun getUsername(): String {
        return email
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isEnabled(): Boolean {
        return true
    }
}
