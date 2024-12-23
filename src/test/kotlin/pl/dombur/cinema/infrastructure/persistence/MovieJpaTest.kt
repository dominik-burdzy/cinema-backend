package pl.dombur.cinema.infrastructure.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import pl.dombur.cinema.config.BaseJpaTest
import pl.dombur.cinema.infrastructure.repository.MovieRepository
import pl.dombur.cinema.utils.TestMovieFactory
import java.util.UUID

class MovieJpaTest : BaseJpaTest() {
    @Autowired
    private lateinit var movieRepository: MovieRepository

    @AfterEach
    fun cleanUp() {
        movieRepository.deleteAll()
    }

    @Test
    fun `should save movie successfully`() {
        // given
        val entity = TestMovieFactory.movieEntity()

        // when
        val result = movieRepository.save(entity)

        // then
        assertThat(result).isNotNull
        assertThat(result.referenceId).isNotNull
        assertThat(result.imdbId).isEqualTo(entity.imdbId)
        assertThat(result.title).isEqualTo(entity.title)
        assertThat(result.createdAt).isNotNull
        assertThat(result.modifiedAt).isNotNull
    }

    @Test
    fun `should find movie by reference id`() {
        // given
        val movieReferenceId = UUID.randomUUID()
        val entity =
            movieRepository.save(
                TestMovieFactory.movieEntity(referenceId = movieReferenceId),
            )

        // when
        val result = movieRepository.findByReferenceId(movieReferenceId)

        // then
        assertThat(result).isNotNull
        assertThat(result).isEqualTo(entity)
    }
}
