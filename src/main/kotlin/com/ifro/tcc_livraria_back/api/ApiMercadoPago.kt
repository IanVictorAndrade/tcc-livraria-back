package com.ifro.tcc_livraria_back.api

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.preference.*
import com.mercadopago.resources.preference.Preference
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class ApiMercadoPago {

    init {
        MercadoPagoConfig.setAccessToken("TEST-2968039211870550-080607-86bc28bdde12c632c52bb31dc368f3d4-2751690")
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
                .build()
        )
        .autoReturn("approved")
        .build()

    private val preference: Preference = client.create(preferenceRequest)

    @Bean
    fun getPreferenceInitPoint(): String {
        return preference.initPoint
    }
}