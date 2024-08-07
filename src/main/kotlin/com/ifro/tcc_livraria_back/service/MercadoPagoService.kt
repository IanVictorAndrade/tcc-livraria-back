package com.ifro.tcc_livraria_back.service

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.preference.*
import com.mercadopago.resources.preference.Preference
import org.springframework.stereotype.Service

@Service
class MercadoPagoService {

    init {
        MercadoPagoConfig.setAccessToken("TEST-2968039211870550-080607-86bc28bdde12c632c52bb31dc368f3d4-2751690")
    }

    private val client = PreferenceClient()


    fun linkPagamento(item: PreferenceItemRequest): String {

        val preferenceRequest: PreferenceRequest = PreferenceRequest.builder()
            .items(mutableListOf(item))
            .backUrls(
                PreferenceBackUrlsRequest.builder()
                    .success("http://yourwebsite.com/success")
                    .failure("http://yourwebsite.com/failure")
                    .build()
            )
            .autoReturn("approved")
            .build()

        val preference: Preference = client.create(preferenceRequest)

        return preference.initPoint
    }
}