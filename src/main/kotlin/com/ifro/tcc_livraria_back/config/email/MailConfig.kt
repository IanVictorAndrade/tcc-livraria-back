package com.ifro.tcc_livraria_back.config.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class MailConfig {

    @Value ("\${spring.mail.username}")
    private val emailSender : String? = null

    @Value ("\${spring.mail.password}")
    private val senha : String? = null

    @Value ("\${spring.mail.host}")
    private val hostName : String? = null

    @Value ("\${spring.mail.port}")
    private val porta : Int = 0

    @Value ("\${spring.mail.protocol}")
    private val protocolo : String? = null

    @Bean
    fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl().apply {
            host = hostName
            port = porta
            username = emailSender
            password = senha
            protocol = protocolo
            javaMailProperties = Properties().apply {
                put("mail.transport.protocol", "smtp")
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.starttls.required", "true")
                put("mail.smtp.connectiontimeout", "5000")
                put("mail.smtp.timeout", "5000")
                put("mail.smtp.writetimeout", "5000")
                put("mail.smtp.ssl.trust", "smtp.gmail.com")
                put("mail.debug", "true") // Ativa o log de debug para SMTP
            }
        }
    }
}
