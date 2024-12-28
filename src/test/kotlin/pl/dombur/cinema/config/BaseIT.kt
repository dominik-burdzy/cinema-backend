package pl.dombur.cinema.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import pl.dombur.cinema.infrastructure.external.omdb.OmdbClient
import pl.dombur.cinema.infrastructure.repository.MovieRepository
import pl.dombur.cinema.infrastructure.repository.ShowRepository

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BaseIT : PostgresTestContainerConfig() {
    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    protected lateinit var mapper: ObjectMapper

    @Autowired
    protected lateinit var movieRepository: MovieRepository

    @Autowired
    protected lateinit var showRepository: ShowRepository

    @MockitoBean
    protected lateinit var omdbClient: OmdbClient

    internal fun <T> MockHttpServletRequestBuilder.withBody(body: T): MockHttpServletRequestBuilder =
        this.content(mapper.writeValueAsString(body)).contentType(MediaType.APPLICATION_JSON)

    internal final inline fun <reified T> ResultActions.andParsedResponse(): T =
        andReturn().response.contentAsString.let { mapper.readValue(it) }
}
