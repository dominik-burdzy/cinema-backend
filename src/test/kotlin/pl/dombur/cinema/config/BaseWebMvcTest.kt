package pl.dombur.cinema.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import pl.dombur.cinema.common.web.ErrorController

@WebMvcTest
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [
        WebSecurityConfig::class,
        ErrorController::class,
    ],
)
abstract class BaseWebMvcTest {
    @Autowired
    protected lateinit var mapper: ObjectMapper

    internal fun <T> MockHttpServletRequestBuilder.withBody(body: T): MockHttpServletRequestBuilder =
        this.content(mapper.writeValueAsString(body)).contentType(MediaType.APPLICATION_JSON)

    internal inline fun <reified T> ResultActions.andParsedResponse(): T =
        andReturn().response.contentAsString.let { mapper.readValue(it) }
}
