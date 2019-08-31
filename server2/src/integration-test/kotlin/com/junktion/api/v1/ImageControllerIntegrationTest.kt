package com.junktion.api.v1

import com.junktion.JunktionAdminConfig
import com.junktion.Oauth2ClientConfig
import io.kotlintest.extensions.TestListener
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.json.JacksonJsonParser
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.mock.web.MockMultipartFile




@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerIntegrationTest: StringSpec() {

	override fun listeners(): List<TestListener> = listOf(SpringListener)

	@Autowired private lateinit var mockMvc: MockMvc
	@Autowired private lateinit var oauth2ClientConfig: Oauth2ClientConfig
	@Autowired private lateinit var junktionAdminConfig: JunktionAdminConfig

	init {
		"通常アクセスで401になること" {
			val requestWithoutAccessToken = multipart("/api/v1/images/upload2")
					.file(MockMultipartFile("upload_file", "filename.png", "image/png", "test data".toByteArray()))
					.param("tag", "some_tag")
			mockMvc.perform(requestWithoutAccessToken)
					.andDo(print())
					.andExpect(status().isUnauthorized)
		}
		"認証後アクセスで200になること" {
			val accessToken = obtainAccessToken()
			MockMultipartFile("multipartFile", "filename.png", "image/png", "test data".toByteArray())
			val request = multipart("/api/v1/images/upload2")
					.file("upload_file", "test data".toByteArray())
					.param("tag", "some_tag")
					.header("Authorization", "Bearer $accessToken")
			mockMvc.perform(request)
					.andDo(print())
					.andExpect(status().isOk)
		}
		"バリデーションエラーで400になること" {
			val accessToken = obtainAccessToken()
			val gettingWithAccessToken = multipart("/api/v1/images/upload2")
					.header("Authorization", "Bearer $accessToken")
					.param("tag", "some_tag")
			mockMvc.perform(gettingWithAccessToken)
					.andDo(print())
					.andExpect(status().isBadRequest)
		}
	}

	private fun obtainAccessToken(): String {

		val params = LinkedMultiValueMap<String, String>().apply {
			add("grant_type", "password")
			add("username", junktionAdminConfig.userId)
			add("password", junktionAdminConfig.password)
		}
		val result = mockMvc.perform(post("/oauth/token")
				.params(params)
				.with(httpBasic(oauth2ClientConfig.clientId, oauth2ClientConfig.clientSecret))
				.accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk)
				.andExpect(content().contentType("application/json;charset=UTF-8"))

		val resultString = result.andReturn().response.contentAsString
		val resultJson = JacksonJsonParser().parseMap(resultString)
		return resultJson["access_token"].toString()
	}
}
