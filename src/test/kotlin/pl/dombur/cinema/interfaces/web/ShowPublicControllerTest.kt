package pl.dombur.cinema.interfaces.web

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.dombur.cinema.application.ShowService
import pl.dombur.cinema.config.BaseWebMvcTest
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.interfaces.web.dto.ShowResult
import pl.dombur.cinema.utils.TestShowFactory
import java.time.LocalDate
import java.util.UUID

@ContextConfiguration(classes = [ShowPublicController::class])
class ShowPublicControllerTest : BaseWebMvcTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var showService: ShowService

    @Test
    fun `GIVEN some shows WHEN list with filters THEN should return them successfully`() {
        // given
        val shows =
            listOf(
                TestShowFactory.showModel(),
                TestShowFactory.showModel(),
                TestShowFactory.showModel(),
                TestShowFactory.showModel(),
            )

        val movieReferenceIds = shows.map { it.movieReferenceId }.toSet()
        val type = ShowType.VIP
        val showDate = LocalDate.now()

        given(showService.findAll(any())).willReturn(shows)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/shows")
                        .queryParam("movieReferenceIds", *movieReferenceIds.map { it.toString() }.toTypedArray())
                        .queryParam("type", type.name)
                        .queryParam("showDate", showDate.toString()),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<List<ShowResult>>()

        // then
        assertThat(result).hasSize(shows.size)
        assertThat(result).extracting("referenceId").containsExactlyInAnyOrderElementsOf(shows.map { it.referenceId })

        verify(showService, times(1)).findAll(
            check { cmd ->
                assertThat(cmd.movieReferenceIds).isEqualTo(movieReferenceIds)
                assertThat(cmd.type).isEqualTo(type)
                assertThat(cmd.showDate).isEqualTo(showDate)
            },
        )
    }

    @Test
    fun `GIVEN some shows WHEN list with invalid filter THEN should return 401`() {
        // given
        val invalidType = "INVALID"

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/public/shows")
                    .queryParam("type", invalidType),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `GIVEN a show WHEN get one THEN should return it successfully`() {
        // given
        val show = TestShowFactory.showModel()

        given(showService.findOne(eq(show.referenceId))).willReturn(show)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/v1/public/shows/${show.referenceId}"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<ShowResult>()

        // then
        assertThat(result).isEqualTo(show.toResult())
    }

    @Test
    fun `GIVEN no show WHEN get one THEN should return 404`() {
        // given
        val referenceId = UUID.randomUUID()

        given(showService.findOne(eq(referenceId))).willThrow(EntityNotFoundException("not found"))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/public/shows/$referenceId"),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
