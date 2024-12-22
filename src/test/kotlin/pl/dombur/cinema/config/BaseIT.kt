package pl.dombur.cinema.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class BaseIT : PostgresTestContainerConfig() {
    @Autowired
    protected lateinit var webTestClient: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @AfterEach
    fun cleanup() {}

    fun MockHttpServletRequestDsl.jsonBody(body: Any) {
        this.content = if (body is String) body else objectMapper.writeValueAsString(body)
        this.accept = MediaType.APPLICATION_JSON
        this.contentType = MediaType.APPLICATION_JSON
    }
}
