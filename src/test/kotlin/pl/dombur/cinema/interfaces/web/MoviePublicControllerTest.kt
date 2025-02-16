package pl.dombur.cinema.interfaces.web

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.dombur.cinema.application.MovieService
import pl.dombur.cinema.config.BaseWebMvcTest
import pl.dombur.cinema.domain.movie.MovieMapper
import pl.dombur.cinema.interfaces.web.dto.MovieDetailedResult
import pl.dombur.cinema.interfaces.web.dto.MovieSummaryResult
import pl.dombur.cinema.utils.TestMovieFactory
import java.util.UUID

@ContextConfiguration(classes = [MoviePublicController::class])
class MoviePublicControllerTest : BaseWebMvcTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var movieService: MovieService

    @Test
    fun `GIVEN some movies WHEN list THEN should return them successfully`() {
        // given
        val movies =
            listOf(
                TestMovieFactory.movieModel(),
                TestMovieFactory.movieModel(),
                TestMovieFactory.movieModel(),
                TestMovieFactory.movieModel(),
                TestMovieFactory.movieModel(),
            )

        given(movieService.findAll()).willReturn(movies)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get("/api/v1/public/movies"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<List<MovieSummaryResult>>()

        // then
        assertThat(result).hasSize(movies.size)
        assertThat(result).extracting("referenceId").containsExactlyInAnyOrderElementsOf(movies.map { it.referenceId })
    }

    @Test
    fun `GIVEN a movie WHEN get one THEN should return it successfully`() {
        // given
        val movie = TestMovieFactory.movieDetailedModel()

        given(movieService.findOne(eq(movie.referenceId))).willReturn(movie)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/movies/${movie.referenceId}"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<MovieDetailedResult>()

        // then
        assertThat(result).isEqualTo(MovieMapper.toDetailedResult(movie))
    }

    @Test
    fun `GIVEN no movie WHEN get one THEN should return 404`() {
        // given
        val referenceId = UUID.randomUUID()

        given(movieService.findOne(eq(referenceId))).willThrow(EntityNotFoundException("not found"))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/public/movies/$referenceId"),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `GIVEN a movie WHEN rate THEN should add rating successfully`() {
        // given
        val referenceId = UUID.randomUUID()
        val form =
            TestMovieFactory.rateMovieForm(
                rating = 6,
                comment = "Great movie! But could be better...",
            )

        doNothing().`when`(movieService).rate(any())

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/movies/$referenceId/ratings")
                    .withBody(form),
            ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `GIVEN no movie WHEN rate THEN should return 404`() {
        // given
        val referenceId = UUID.randomUUID()
        val form = TestMovieFactory.rateMovieForm()

        given(movieService.rate(any())).willThrow(EntityNotFoundException("not found"))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/movies/$referenceId/ratings")
                    .withBody(form),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `GIVEN invalid form WHEN rate THEN should return 400`() {
        // given
        val referenceId = UUID.randomUUID()
        val form =
            TestMovieFactory.rateMovieForm(
                rating = 11,
                comment = "AMAZING!!!",
            )

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/public/movies/$referenceId/ratings")
                    .withBody(form),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
