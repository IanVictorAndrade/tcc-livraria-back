package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.dto.PreferenceItemDTO
import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.preference.PreferenceBackUrlsRequest
import com.mercadopago.client.preference.PreferenceClient
import com.mercadopago.client.preference.PreferenceItemRequest
import com.mercadopago.client.preference.PreferenceRequest
import com.mercadopago.resources.preference.Preference
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class MercadoPagoService {

    init {
        MercadoPagoConfig.setAccessToken("TEST-2968039211870550-080607-86bc28bdde12c632c52bb31dc368f3d4-2751690")
    }

    private val client = PreferenceClient()

    @Value("\${spring_dominio_front}")
    private val dominioFront: String? = null


    fun linkPagamento(item: PreferenceItemDTO): String {
        val successUrl = "${dominioFront}/pagamentoSucesso?livroId=${item.id}"
        val failureUrl = "${dominioFront}/pagamentoFalho"

        val preferenceRequest: PreferenceRequest = PreferenceRequest.builder()
            .items(mutableListOf(item.toPreferenceItemRequest()))
            .backUrls(
                PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .build()
            )
            .autoReturn("approved")
            .build()

        val preference: Preference = client.create(preferenceRequest)
        return preference.initPoint
    }

    fun PreferenceItemDTO.toPreferenceItemRequest(): PreferenceItemRequest {
        return PreferenceItemRequest.builder()
            .id(this.id)
            .title(this.title)
            .description(this.description)
            .categoryId("entertainment")
            .quantity(this.quantity)
            .currencyId(this.currencyId)
            .unitPrice(BigDecimal(this.unitPrice.toString()))
            .build()
    }
}