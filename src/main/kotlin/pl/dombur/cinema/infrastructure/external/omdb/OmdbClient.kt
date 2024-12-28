package pl.dombur.cinema.infrastructure.external.omdb

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder
import pl.dombur.cinema.config.CachingConfig.Companion.OMDB_MOVIE_CACHE
import pl.dombur.cinema.config.OmdbClientProperties
import pl.dombur.cinema.domain.movie.MovieDataProvider
import pl.dombur.cinema.infrastructure.external.omdb.dto.OmdbErrorResponse
import pl.dombur.cinema.infrastructure.external.omdb.dto.OmdbMovieResponse
import pl.dombur.cinema.infrastructure.external.omdb.exception.OmdbClientException
import java.net.URI
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Component
class OmdbClient(
    restClientBuilder: RestClient.Builder,
    private val objectMapper: ObjectMapper,
    private val properties: OmdbClientProperties,
) : MovieDataProvider {
    private val restClient: RestClient =
        restClientBuilder
            .baseUrl(properties.url)
            .build()

    companion object {
        private const val API_KEY_QUERY_PARAM_NAME = "apikey"
        private const val ID_QUERY_PARAM_NAME = "i"
    }

    @Cacheable(value = [OMDB_MOVIE_CACHE], key = "#id")
    override fun getMovie(id: String): MovieData {
        logger.info { "Fetching movie with id: $id from OMDb service" }
        val response =
            callApiGet<OmdbMovieResponse> { uriBuilder ->
                uriBuilder
                    .queryParam(API_KEY_QUERY_PARAM_NAME, properties.apiKey)
                    .queryParam(ID_QUERY_PARAM_NAME, id)
                    .build()
            }

        return response.toMovieData()
    }

    @CacheEvict(value = [OMDB_MOVIE_CACHE], allEntries = true)
    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1, initialDelay = 1)
    fun clearCache() {
        logger.debug { "Evicting cache of OMDb movies" }
    }

    private inline fun <reified T : Any> callApiGet(noinline uriFunction: (UriBuilder) -> URI): T {
        val response =
            runCatching {
                restClient
                    .get()
                    .uri(uriFunction)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(String::class.java)
            }.getOrElse {
                logger.error(it) { "Failed to call OMDb service" }
                throw OmdbClientException("Failed to call OMDb service because of: ${it.message}")
            } ?: run {
                logger.error { "Empty response when call OMDb service" }
                throw OmdbClientException("Empty response when call OMDb service")
            }

        return parseResponseBody(response)
    }

    private inline fun <reified T : Any> parseResponseBody(response: String): T {
        if (isBodyInstanceOf<T>(response)) {
            return objectMapper.readValue(response, T::class.java)
        }

        if (isBodyInstanceOf<OmdbErrorResponse>(response)) {
            val error = objectMapper.readValue(response, OmdbErrorResponse::class.java)
            logger.error { "Failed to fetch data from OMDb service because of: ${error.error}" }
            throw OmdbClientException("Failed to fetch data from OMDb service because of: ${error.error}")
        }

        throw OmdbClientException("Failed to parse response from OMDb service: $response")
    }

    private inline fun <reified T : Any> isBodyInstanceOf(response: String): Boolean {
        try {
            objectMapper.readValue(response, T::class.java)
            return true
        } catch (_: Exception) {
            return false
        }
    }
}
