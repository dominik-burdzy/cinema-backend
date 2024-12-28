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
        val ratings =
            mutableListOf(
                MovieRatingEntity(
                    rating = 5,
                    comment = "Test comment",
                ),
                MovieRatingEntity(
                    rating = 4,
                    comment = "Test comment 2",
                ),
                MovieRatingEntity(
                    rating = 3,
                    comment = "Test comment 3",
                ),
            )

        val entity = TestMovieFactory.movieEntity(ratings = ratings)

        // when
        val result = movieRepository.save(entity)

        // then
        assertThat(result).isNotNull
        assertThat(result.referenceId).isNotNull
        assertThat(result.imdbId).isEqualTo(entity.imdbId)
        assertThat(result.title).isEqualTo(entity.title)
        assertThat(result.createdAt).isNotNull
        assertThat(result.modifiedAt).isNotNull
        assertThat(result.ratings).hasSize(3)
        assertThat(result.ratings).extracting("rating").containsExactly(5, 4, 3)
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
