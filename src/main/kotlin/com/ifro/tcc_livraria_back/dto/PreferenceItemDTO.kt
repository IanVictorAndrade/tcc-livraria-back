package com.ifro.tcc_livraria_back.dto

import java.math.BigDecimal

data class PreferenceItemDTO (
    val id: String,
    val title: String,
    val description: String?,
    val quantity: Int,
    val currencyId: String,
    val unitPrice: BigDecimal
)