package pl.dombur.cinema.infrastructure.external.omdb

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.util.ResourceUtils
import pl.dombur.cinema.config.OmdbClientProperties
import pl.dombur.cinema.infrastructure.external.omdb.exception.OmdbClientException
import java.time.LocalDate

@ActiveProfiles("test")
@AutoConfigureMockRestServiceServer
@RestClientTest(OmdbClient::class)
@Import(OmdbClientProperties::class)
class OmdbClientTest {
    @Autowired
    private lateinit var client: OmdbClient

    @Autowired
    private lateinit var server: MockRestServiceServer

    @Test
    fun `GIVEN imdb ID WHEN getMovie THEN return movie data successfully`() {
        // given
        val imdbId = "tt1234567"

        server
            .expect(requestTo("http://www.omdbapi.com?apikey=testApiKey&i=tt1234567"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    ResourceUtils.getFile("classpath:fixtures/omdb/success.json").readText(),
                    MediaType.APPLICATION_JSON,
                ),
            )

        // when
        val result = client.getMovie(imdbId)

        // then
        assertThat(result.title).isEqualTo("The Fast and the Furious")
        assertThat(
            result.description,
        ).isEqualTo(
            "Los Angeles police officer Brian O'Conner must decide where his loyalty really lies when he becomes enamored with the street racing world he has been sent undercover to destroy.",
        )
        assertThat(result.releaseDate).isEqualTo(LocalDate.of(2001, 6, 22))
        assertThat(result.runtime).isEqualTo("106 min")
        assertThat(result.genre).isEqualTo("Action, Crime, Thriller")
        assertThat(result.director).isEqualTo("Rob Cohen")
        assertThat(result.actors).isEqualTo("Paul Walker, Vin Diesel, Michelle Rodriguez, Jordana Brewster")
        assertThat(result.country).isEqualTo("USA, Germany")
        assertThat(result.imdbRating).isEqualTo("6.8")
    }

    @Test
    fun `GIVEN imdb ID WHEN getMovie but there is no response THEN should throw OmdbClientException`() {
        // given
        val imdbId = "tt1234567"

        server
            .expect(requestTo("http://www.omdbapi.com?apikey=testApiKey&i=$imdbId"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess())

        // when / then
        val exception =
            assertThrows<OmdbClientException> {
                client.getMovie(imdbId)
            }

        assertThat(exception.message).isEqualTo("Empty response when call OMDb service")
    }

    @Test
    fun `GIVEN imdb ID WHEN getMovie but OMDb returns error THEN should throw OmdbClientException`() {
        // given
        val imdbId = "tt1234567"

        server
            .expect(requestTo("http://www.omdbapi.com?apikey=testApiKey&i=tt1234567"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    ResourceUtils.getFile("classpath:fixtures/omdb/error.json").readText(),
                    MediaType.APPLICATION_JSON,
                ),
            )

        // when / then
        val exception =
            assertThrows<OmdbClientException> {
                client.getMovie(imdbId)
            }

        assertThat(
            exception.message,
        ).isEqualTo("Failed to fetch data from OMDb service because of: Error getting data.")
    }

    @Test
    fun `GIVEN imdb ID WHEN getMovie but provided ID is invalid THEN should throw OmdbClientException`() {
        // given
        val imdbId = "tt1234567"

        server
            .expect(requestTo("http://www.omdbapi.com?apikey=testApiKey&i=tt1234567"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    ResourceUtils.getFile("classpath:fixtures/omdb/invalid_id.json").readText(),
                    MediaType.APPLICATION_JSON,
                ),
            )

        // when / then
        val exception =
            assertThrows<OmdbClientException> {
                client.getMovie(imdbId)
            }

        assertThat(exception.message).isEqualTo("Failed to fetch data from OMDb service because of: Incorrect IMDb ID.")
    }
}
