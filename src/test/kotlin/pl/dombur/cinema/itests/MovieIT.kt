package pl.dombur.cinema.itests

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.dombur.cinema.config.BaseIT
import pl.dombur.cinema.interfaces.web.dto.MovieDetailedResult
import pl.dombur.cinema.interfaces.web.dto.MovieSummaryResult
import pl.dombur.cinema.utils.TestMovieFactory

class MovieIT : BaseIT() {
    @Test
    fun `GIVEN some movies WHEN I am a public user THEN should be able to get movie details and rate the movie`() {
        // when: fetch all movies
        val movies =
            mvc
                .perform(
                    MockMvcRequestBuilders.get("/api/v1/public/movies"),
                ).andExpect(status().isOk)
                .andParsedResponse<List<MovieSummaryResult>>()

        // then: should have some movies stored already in the database
        assertThat(movies).hasSize(9)

        // given: a movie
        val movieReferenceId = movies.first()
        val movie = movieRepository.findByReferenceId(movieReferenceId.referenceId)!!

        // and: OMDb returning movie details
        val data = TestMovieFactory.movieData()

        given(omdbClient.getMovie(eq(movie.imdbId))).willReturn(data)

        // when: fetch movie details
        val movieDetailedResult =
            mvc
                .perform(
                    MockMvcRequestBuilders.get("/api/v1/public/movies/${movie.referenceId}"),
                ).andExpect(status().isOk)
                .andParsedResponse<MovieDetailedResult>()

        // then: should return movie details
        assertThat(movieDetailedResult.referenceId).isEqualTo(movie.referenceId)
        assertThat(movieDetailedResult.title).isEqualTo(data.title)
        assertThat(movieDetailedResult.description).isEqualTo(data.description)
        assertThat(movieDetailedResult.releaseDate).isEqualTo(data.releaseDate)
        assertThat(movieDetailedResult.runtime).isEqualTo(data.runtime)
        assertThat(movieDetailedResult.genre).isEqualTo(data.genre)
        assertThat(movieDetailedResult.director).isEqualTo(data.director)
        assertThat(movieDetailedResult.actors).isEqualTo(data.actors)
        assertThat(movieDetailedResult.country).isEqualTo(data.country)
        assertThat(movieDetailedResult.imdbRating).isEqualTo(data.imdbRating)
        assertThat(movieDetailedResult.cinemaRating).isNull()

        // given: some ratings
        val ratingForm1 =
            TestMovieFactory.rateMovieForm(
                rating = 8,
                comment = "Great movie!",
            )
        val ratingForm2 =
            TestMovieFactory.rateMovieForm(
                rating = 6,
                comment = "Good movie!",
            )
        val ratingForm3 =
            TestMovieFactory.rateMovieForm(
                rating = 3,
                comment = "Average movie!",
            )

        // when: rate the movie multiple times
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/movies/${movie.referenceId}/ratings")
                    .withBody(ratingForm1),
            ).andExpect(status().isOk)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/movies/${movie.referenceId}/ratings")
                    .withBody(ratingForm2),
            ).andExpect(status().isOk)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/movies/${movie.referenceId}/ratings")
                    .withBody(ratingForm3),
            ).andExpect(status().isOk)

        // and: fetch movie details again
        val movieDetailedResultRated =
            mvc
                .perform(
                    MockMvcRequestBuilders.get("/api/v1/public/movies/${movie.referenceId}"),
                ).andExpect(status().isOk)
                .andParsedResponse<MovieDetailedResult>()

        // then: should return movie details with ratings
        assertThat(movieDetailedResultRated.cinemaRating).isEqualTo(5.7)
    }
}
