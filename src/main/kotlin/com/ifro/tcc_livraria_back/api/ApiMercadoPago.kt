package com.ifro.tcc_livraria_back.api

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.common.AddressRequest
import com.mercadopago.client.common.IdentificationRequest
import com.mercadopago.client.common.PhoneRequest
import com.mercadopago.client.preference.*
import java.math.BigDecimal

class ApiMercadoPago {

    private val mercadoPagoConfig = MercadoPagoConfig.setAccessToken("TEST-1234567890")

    private val client = PreferenceClient()

    private val itemRequest: PreferenceItemRequest? = PreferenceItemRequest.builder()
        .id("1234")
        .title("Dummy Title")
        .description("Dummy description")
        .pictureUrl("http://www.myapp.com/myimage.jpg")
        .categoryId("car_electronics")
        .quantity(1)
        .currencyId("BRL")
        .unitPrice(BigDecimal("10"))
        .build()

    private val items = mutableListOf(itemRequest)

    private val freeMethod: PreferenceFreeMethodRequest? = PreferenceFreeMethodRequest.builder()
        .id(1L).build()
    private val freeMethodList = mutableListOf(freeMethod)

    private val excludedPaymentTypes = mutableListOf(
        PreferencePaymentTypeRequest.builder().id("ticket").build()
    )

    private val excludedPaymentMethods = mutableListOf(
        PreferencePaymentMethodRequest.builder().id("").build()
    )

    private val preferenceRequest: PreferenceRequest? = PreferenceRequest.builder()
        .backUrls(
            PreferenceBackUrlsRequest.builder()
                .success("http://test.com/success")
                .failure("http://test.com/failure")
                .pending("http://test.com/pending")
                .build()
        )
        .differentialPricing(
            PreferenceDifferentialPricingRequest.builder()
                .id(1L)
                .build()
        )
        .expires(false)
        .items(items)
        .marketplaceFee(BigDecimal("0"))
        .payer(
            PreferencePayerRequest.builder()
                .name("Test")
                .surname("User")
                .email("your_test_email@example.com")
                .phone(PhoneRequest.builder().areaCode("11").number("4444-4444").build())
                .identification(
                    IdentificationRequest.builder().type("CPF").number("19119119100").build()
                )
                .address(
                    AddressRequest.builder()
                        .zipCode("06233200")
                        .streetName("Street")
                        .streetNumber("123")
                        .build()
                )
                .build()
        )
        .additionalInfo("Discount: 12.00")
        .autoReturn("all")
        .binaryMode(true)
        .externalReference("1643827245")
        .marketplace("marketplace")
        .notificationUrl("http://notificationurl.com")
        .operationType("regular_payment")
        .paymentMethods(
            PreferencePaymentMethodsRequest.builder()
                .defaultPaymentMethodId("master")
                .excludedPaymentTypes(excludedPaymentTypes)
                .excludedPaymentMethods(excludedPaymentMethods)
                .installments(5)
                .defaultInstallments(1)
                .build()
        )
        .shipments(
            PreferenceShipmentsRequest.builder()
                .mode("custom")
                .localPickup(false)
                .defaultShippingMethod(null)
                .freeMethods(freeMethodList)
                .cost(BigDecimal.TEN)
                .freeShipping(false)
                .dimensions("10x10x20,500")
                .receiverAddress(
                    PreferenceReceiverAddressRequest.builder()
                        .zipCode("06000000")
                        .streetNumber("123")
                        .streetName("Street")
                        .floor("12")
                        .apartment("120A")
                        .build()
                )
                .build()
        )
        .statementDescriptor("Test Store")
        .build()

    val preference = client.create(preferenceRequest)
}

