package pl.dombur.cinema.interfaces.web

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.dombur.cinema.application.ShowService
import pl.dombur.cinema.application.exception.ShowAlreadyDefinedException
import pl.dombur.cinema.config.BaseWebMvcTest
import pl.dombur.cinema.interfaces.web.dto.ShowResult
import pl.dombur.cinema.utils.TestShowFactory
import java.util.UUID

@ContextConfiguration(classes = [ShowInternalController::class])
class ShowInternalControllerTest : BaseWebMvcTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var showService: ShowService

    @Test
    fun `GIVEN create form WHEN create a new show THEN should create it successfully`() {
        // given
        val form = TestShowFactory.createShowForm()
        val created =
            TestShowFactory.showModel(
                movieReferenceId = form.movieReferenceId,
                price = form.price,
                type = form.type,
                schedule = form.schedule,
            )

        given(showService.create(any())).willReturn(created)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .post("/api/v1/internal/shows")
                        .withBody(form),
                ).andExpect(MockMvcResultMatchers.status().isCreated)
                .andParsedResponse<ShowResult>()

        // then
        assertThat(result).isEqualTo(created.toResult())
    }

    @Test
    fun `GIVEN create form WHEN create a new show but it already exists for given movie THEN should return 409`() {
        // given
        val form = TestShowFactory.createShowForm()

        given(showService.create(any())).willThrow(ShowAlreadyDefinedException(form.movieReferenceId, form.type))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/internal/shows")
                    .withBody(form),
            ).andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `GIVEN create form WHEN create a new show but related movie doesn't exist THEN should return 404`() {
        // given
        val form = TestShowFactory.createShowForm()

        given(showService.create(any())).willThrow(EntityNotFoundException("not found"))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/internal/shows")
                    .withBody(form),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `GIVEN update form WHEN update an exising show THEN should update it successfully`() {
        // given
        val referenceId = UUID.randomUUID()
        val form = TestShowFactory.updateShowForm()
        val updated =
            TestShowFactory.showModel(
                referenceId = referenceId,
                price = form.price,
                schedule = form.schedule,
            )

        given(showService.update(any())).willReturn(updated)

        // when
        val result =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .put("/api/v1/internal/shows/$referenceId")
                        .withBody(form),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                .andParsedResponse<ShowResult>()

        // then
        assertThat(result).isEqualTo(updated.toResult())
    }

    @Test
    fun `GIVEN update form WHEN try to update non-exising show THEN should return 404`() {
        // given
        val referenceId = UUID.randomUUID()
        val form = TestShowFactory.updateShowForm()

        given(showService.update(any())).willThrow(EntityNotFoundException("not found"))

        // when / then
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put("/api/v1/internal/shows/$referenceId")
                    .withBody(form),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
