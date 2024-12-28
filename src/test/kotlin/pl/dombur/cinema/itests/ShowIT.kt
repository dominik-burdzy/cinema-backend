package pl.dombur.cinema.itests

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.dombur.cinema.config.BaseIT
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.interfaces.web.dto.ShowResult
import pl.dombur.cinema.utils.TestShowFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ShowIT : BaseIT() {
    @Test
    fun `GIVEN some movies WHEN I am admin user THEN should be able to manage show times`() {
        // given: a movie
        val movie = movieRepository.findAll().first()

        // and: show schedule forms
        val form2D =
            TestShowFactory.createShowForm(
                movieReferenceId = movie.referenceId,
                type = ShowType._2D,
                price = BigDecimal(12.0),
                schedule =
                    mapOf(
                        LocalDate.now().plusDays(1) to
                            listOf(LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(15, 0)),
                        LocalDate.now().plusDays(2) to listOf(LocalTime.of(18, 0), LocalTime.of(21, 30)),
                        LocalDate.now().plusDays(3) to
                            listOf(LocalTime.of(8, 30), LocalTime.of(15, 0), LocalTime.of(17, 0)),
                        LocalDate.now().plusDays(4) to listOf(LocalTime.of(11, 15), LocalTime.of(13, 0)),
                    ),
            )

        val form3D =
            TestShowFactory.createShowForm(
                movieReferenceId = movie.referenceId,
                type = ShowType._3D,
                price = BigDecimal(15.0),
                schedule =
                    mapOf(
                        LocalDate.now().plusDays(1) to listOf(LocalTime.of(14, 30), LocalTime.of(18, 0)),
                        LocalDate.now().plusDays(2) to listOf(LocalTime.of(12, 0), LocalTime.of(23, 30)),
                        LocalDate.now().plusDays(3) to listOf(LocalTime.of(16, 30), LocalTime.of(18, 0)),
                        LocalDate.now().plusDays(4) to listOf(LocalTime.of(8, 15), LocalTime.of(10, 0)),
                    ),
            )

        // when: create a 2D show schedule
        val show2DCreated =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .post("/api/v1/internal/shows")
                        .withBody(form2D),
                ).andExpect(MockMvcResultMatchers.status().isCreated)
                .andParsedResponse<ShowResult>()

        // and: create a 3D show schedule
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/internal/shows")
                    .withBody(form3D),
            ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andParsedResponse<ShowResult>()

        // then: show schedule should be created
        val showsResult =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/shows"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<List<ShowResult>>()

        // and: show schedules should be the same as the forms
        assertThat(showsResult).hasSize(2)

        val show2DResult = showsResult.first { it.type == form2D.type }
        assertThat(show2DResult.movieReferenceId).isEqualTo(movie.referenceId)
        assertThat(show2DResult.type).isEqualTo(form2D.type)
        assertThat(show2DResult.price).isEqualByComparingTo(form2D.price)
        assertThat(show2DResult.schedule).isEqualTo(form2D.schedule)

        val show3DResult = showsResult.first { it.type == form3D.type }
        assertThat(show3DResult.movieReferenceId).isEqualTo(movie.referenceId)
        assertThat(show3DResult.type).isEqualTo(form3D.type)
        assertThat(show3DResult.price).isEqualByComparingTo(form3D.price)
        assertThat(show3DResult.schedule).isEqualTo(form3D.schedule)

        // given: update 2D show schedule
        val form2DUpdated =
            TestShowFactory.updateShowForm(
                price = BigDecimal(10.0),
                schedule =
                    mapOf(
                        LocalDate.now().plusDays(1) to
                            listOf(LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(15, 0)),
                        LocalDate.now().plusDays(2) to listOf(LocalTime.of(18, 0), LocalTime.of(21, 30)),
                        LocalDate.now().plusDays(3) to
                            listOf(LocalTime.of(8, 30), LocalTime.of(15, 0), LocalTime.of(17, 0)),
                        LocalDate.now().plusDays(4) to listOf(LocalTime.of(11, 15), LocalTime.of(13, 0)),
                        LocalDate.now().plusDays(5) to listOf(LocalTime.of(7, 15), LocalTime.of(21, 0)),
                        LocalDate.now().plusDays(6) to listOf(LocalTime.of(14, 0), LocalTime.of(18, 10)),
                    ),
            )

        // when: update 2D show schedule
        mvc
            .perform(
                MockMvcRequestBuilders
                    .put("/api/v1/internal/shows/${show2DCreated.referenceId}")
                    .withBody(form2DUpdated),
            ).andExpect(MockMvcResultMatchers.status().isOk)

        // then: show schedule should be updated
        val show2DUpdatedResult =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/shows/${show2DCreated.referenceId}"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<ShowResult>()

        assertThat(show2DUpdatedResult.price).isEqualByComparingTo(form2DUpdated.price)
        assertThat(show2DUpdatedResult.schedule).isEqualTo(form2DUpdated.schedule)
    }
}
