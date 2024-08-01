package com.ifro.tcc_livraria_back

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.token.KeyBasedPersistenceTokenService
import org.springframework.security.core.token.SecureRandomFactoryBean
import java.util.*

@SpringBootTest
class TccLivrariaBackApplicationTests {

	@Test
	fun createToken() {
		val service = KeyBasedPersistenceTokenService()
		service.setServerSecret("SECRET123")
		service.setServerInteger(16)
		service.setSecureRandom(SecureRandomFactoryBean().`object`)


		val token = service.allocateToken("ian@gmail.com")
		println(token.extendedInformation)
		println(Date(token.keyCreationTime))
		println(token.key)


		// Wed Jul 31 12:15:03 AMT 2024
		// MTcyMjQ0MjUwMzExMjo3MGI4YWJlYWYxYjRjMGY5NDA1NWEyOWYyYzFjNjE4ODE4ZTg0MGM3OGQ1ZjIyYzNiYzA5MTk4NTBmODY5NTMwOmlhbkBnbWFpbC5jb206ZDgwY2ZjMzczY2FmNTBkZWVhOWY3OWMzM2M0ZTg0Zjg2MzQ0NzRjOTNlYWExNTNkOTY0MDMwNDliNGYxNTc1Y2FjZTUwZWU1YjBmZTRkNDMxNWVkMWM0N2I2NTBjMDBhNzNiZDE3MjYxMTgxZjdkNTc2ODE1ZDc0OTgzOWNjOTI=
	}

	@Test
	fun readToken() {
		val service = KeyBasedPersistenceTokenService()
		service.setServerSecret("SECRET123")
		service.setServerInteger(16)
		service.setSecureRandom(SecureRandomFactoryBean().`object`)


		val rawToken = "MTcyMjQ0MjUwMzExMjo3MGI4YWJlYWYxYjRjMGY5NDA1NWEyOWYyYzFjNjE4ODE4ZTg0MGM3OGQ1ZjIyYzNiYzA5MTk4NTBmODY5NTMwOmlhbkBnbWFpbC5jb206ZDgwY2ZjMzczY2FmNTBkZWVhOWY3OWMzM2M0ZTg0Zjg2MzQ0NzRjOTNlYWExNTNkOTY0MDMwNDliNGYxNTc1Y2FjZTUwZWU1YjBmZTRkNDMxNWVkMWM0N2I2NTBjMDBhNzNiZDE3MjYxMTgxZjdkNTc2ODE1ZDc0OTgzOWNjOTI="

		service.verifyToken(rawToken)
	}

	@Test
	fun readPublicToken() {
		val rawToken = "MTcyMjQ0MjUwMzExMjo3MGI4YWJlYWYxYjRjMGY5NDA1NWEyOWYyYzFjNjE4ODE4ZTg0MGM3OGQ1ZjIyYzNiYzA5MTk4NTBmODY5NTMwOmlhbkBnbWFpbC5jb206ZDgwY2ZjMzczY2FmNTBkZWVhOWY3OWMzM2M0ZTg0Zjg2MzQ0NzRjOTNlYWExNTNkOTY0MDMwNDliNGYxNTc1Y2FjZTUwZWU1YjBmZTRkNDMxNWVkMWM0N2I2NTBjMDBhNzNiZDE3MjYxMTgxZjdkNTc2ODE1ZDc0OTgzOWNjOTI="

		val bytes = Base64.getDecoder().decode(rawToken)
		val token = String(bytes)
		println(token)

		val tokenParts = token.split(":")

		val timestamp = tokenParts[0].toLong()
		val email = tokenParts[2]
		println(Date(timestamp))
		println(email)
	}

}
