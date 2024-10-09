package com.ifro.tcc_livraria_back.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

data class UsuarioDTO (
    val email: String,
    val senha: String?,
    val cpf: String,
    val nome: String,
    @JsonDeserialize(using = RoleDTODeserializer::class)
    val role: RoleDTO?
)

data class RoleDTO(
    var nome: String
)


class RoleDTODeserializer : JsonDeserializer<RoleDTO>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RoleDTO {
        val roleName = p.valueAsString // Obtém o valor da string no JSON
        return RoleDTO(nome = roleName) // Retorna uma instância de RoleDTO com a string como nome
    }
}
