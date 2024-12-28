package pl.dombur.cinema.application

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import pl.dombur.cinema.domain.movie.MovieDataProvider
import pl.dombur.cinema.infrastructure.repository.MovieRepository
import pl.dombur.cinema.utils.TestMovieFactory
import java.util.UUID

class MovieServiceTest {
    private val movieRepository = mock<MovieRepository>()
    private val movieDataProvider = mock<MovieDataProvider>()

    private val movieService = MovieService(movieRepository, movieDataProvider)

    @Test
    fun `GIVEN a movie WHEN findOne THEN should return it with detailed data`() {
        // given
        val entity = TestMovieFactory.movieEntity()
        val data = TestMovieFactory.movieData()

        given(movieRepository.findByReferenceId(entity.referenceId)).willReturn(entity)
        given(movieDataProvider.getMovie(entity.imdbId)).willReturn(data)

        // when
        val result = movieService.findOne(entity.referenceId)

        // then
        assertThat(result.referenceId).isEqualTo(entity.referenceId)
        assertThat(result.title).isEqualTo(data.title)
        assertThat(result.description).isEqualTo(data.description)
        assertThat(result.releaseDate).isEqualTo(data.releaseDate)
        assertThat(result.runtime).isEqualTo(data.runtime)
        assertThat(result.genre).isEqualTo(data.genre)
        assertThat(result.director).isEqualTo(data.director)
        assertThat(result.actors).isEqualTo(data.actors)
        assertThat(result.country).isEqualTo(data.country)
        assertThat(result.imdbRating).isEqualTo(data.imdbRating)
    }

    @Test
    fun `GIVEN no movie WHEN findOne THEN should throw exception if not found`() {
        // given
        val referenceId = UUID.randomUUID()

        given(movieRepository.findByReferenceId(referenceId)).willReturn(null)

        // when
        val exception =
            assertThrows<EntityNotFoundException> {
                movieService.findOne(referenceId)
            }

        // then
        assertThat(exception.message).isEqualTo("Movie with referenceId: $referenceId not found")
    }

    @Test
    fun `GIVEN some movies WHEN findAll THEN should return them successfully`() {
        // given
        val entities =
            listOf(
                TestMovieFactory.movieEntity(),
                TestMovieFactory.movieEntity(),
                TestMovieFactory.movieEntity(),
                TestMovieFactory.movieEntity(),
                TestMovieFactory.movieEntity(),
            )

        given(movieRepository.findAll()).willReturn(entities)

        // when
        val result = movieService.findAll()

        // then
        assertThat(result).hasSize(entities.size)
        assertThat(
            result,
        ).extracting("referenceId").containsExactlyInAnyOrderElementsOf(entities.map { it.referenceId })
    }

    @Test
    fun `GIVEN a movie WHEN rate THEN should add rating to it`() {
        // given
        val entity = TestMovieFactory.movieEntity()
        val cmd = TestMovieFactory.rateMovieCmd(referenceId = entity.referenceId)

        given(movieRepository.findByReferenceId(entity.referenceId)).willReturn(entity)
        given(movieRepository.save(entity)).willAnswer { it.arguments.first() }

        // when
        movieService.rate(cmd)

        // then
        assertThat(entity.ratings).hasSize(1)
        assertThat(entity.ratings.first().rating).isEqualTo(cmd.rating)
        assertThat(entity.ratings.first().comment).isEqualTo(cmd.comment)
    }

    @Test
    fun `GIVEN no movie WHEN rate THEN should throw exception if not found`() {
        // given
        val referenceId = UUID.randomUUID()
        val cmd = TestMovieFactory.rateMovieCmd(referenceId = referenceId)

        given(movieRepository.findByReferenceId(referenceId)).willReturn(null)

        // when
        val exception =
            assertThrows<EntityNotFoundException> {
                movieService.rate(cmd)
            }

        // then
        assertThat(exception.message).isEqualTo("Movie with referenceId: $referenceId not found")
    }
}
