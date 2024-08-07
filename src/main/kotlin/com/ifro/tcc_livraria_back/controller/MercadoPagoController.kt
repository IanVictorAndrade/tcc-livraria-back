package com.ifro.tcc_livraria_back.controller

import com.ifro.tcc_livraria_back.service.MercadoPagoService
import com.mercadopago.client.preference.PreferenceItemRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mercado-pago")
class MercadoPagoController(
    private val mercadoPagoService: MercadoPagoService
) {


    @PostMapping("/link-pagamento")
    fun linkPagamento(
        @RequestBody item: PreferenceItemRequest
    ): String {
        return mercadoPagoService.linkPagamento(item)
    }
}