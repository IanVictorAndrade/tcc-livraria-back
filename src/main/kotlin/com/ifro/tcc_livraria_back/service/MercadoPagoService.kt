package com.ifro.tcc_livraria_back.service

import com.ifro.tcc_livraria_back.dto.PreferenceItemDTO
import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.payment.PaymentPayerRequest
import com.mercadopago.client.preference.PreferenceBackUrlsRequest
import com.mercadopago.client.preference.PreferenceClient
import com.mercadopago.client.preference.PreferenceItemRequest
import com.mercadopago.client.preference.PreferencePayerRequest
import com.mercadopago.client.preference.PreferenceRequest
import com.mercadopago.resources.preference.Preference
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class MercadoPagoService {

    @Value("\${spring_dominio_front}")
    private lateinit var dominioFront: String

    private val client = PreferenceClient()

    init {
        MercadoPagoConfig.setAccessToken("TEST-5607775475862919-072514-11a5b4e994c191a4a09de1039e8a2246-1915814887")
    }

    fun linkPagamento(item: PreferenceItemDTO): String {
        val successUrl = "$dominioFront/pagamentoSucesso?livroId=${item.id}"
        val failureUrl = "$dominioFront/pagamentoFalho"

        val preferenceRequest = PreferenceRequest.builder()
            .items(mutableListOf(item.toPreferenceItemRequest()))
            .backUrls(
                PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .build()
            )
            .autoReturn("approved")
            .build()

        val preference = client.create(preferenceRequest)

        return preference.sandboxInitPoint
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
