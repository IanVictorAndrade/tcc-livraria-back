package com.ifro.tcc_livraria_back.api

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.preference.*
import com.mercadopago.resources.preference.Preference
import java.math.BigDecimal

class ApiMercadoPago {

    init {
        MercadoPagoConfig.setAccessToken("YOUR_ACCESS_TOKEN")
    }

    private val client = PreferenceClient()

    private val itemRequest: PreferenceItemRequest = PreferenceItemRequest.builder()
        .id("1")  // Identificador do livro (pode ser qualquer identificador único)
        .title("O Trono Vazio")  // Título do livro
        .quantity(1)
        .currencyId("BRL")
        .unitPrice(BigDecimal("42.99"))  // Preço do livro
        .build()

    private val preferenceRequest: PreferenceRequest = PreferenceRequest.builder()
        .items(listOf(itemRequest))
        .backUrls(
            PreferenceBackUrlsRequest.builder()
                .success("http://yourwebsite.com/success")
                .failure("http://yourwebsite.com/failure")
                .pending("http://yourwebsite.com/pending")
                .build()
        )
        .autoReturn("approved")
        .build()

    val preference: Preference = client.create(preferenceRequest)

    fun printPaymentUrl() {
        println("Payment URL: ${preference.initPoint}")  // URL para redirecionar o usuário para o Mercado Pago
    }
}