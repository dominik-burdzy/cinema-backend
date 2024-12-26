package pl.dombur.cinema.application

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import pl.dombur.cinema.infrastructure.persistence.ShowEntity
import pl.dombur.cinema.infrastructure.repository.ShowRepository
import pl.dombur.cinema.infrastructure.repository.ShowSpecification
import pl.dombur.cinema.utils.TestShowFactory
import java.util.UUID

class ShowServiceTest {
    private val showRepository = mock<ShowRepository>()

    private val showService = ShowService(showRepository)

    @Test
    fun `GIVEN existing show WHEN update by provided cmd THEN should update show`() {
        // given
        val entity = TestShowFactory.showEntity()
        val cmd = TestShowFactory.updateShowCmd(referenceId = entity.referenceId)

        given(showRepository.findByReferenceId(cmd.referenceId)).willReturn(entity)
        given(showRepository.save(entity)).willAnswer { it.arguments[0] as ShowEntity }

        // when
        val result = showService.update(cmd)

        // then
        assertThat(result.referenceId).isEqualTo(entity.referenceId)
        assertThat(result.price).isEqualTo(cmd.price)
        assertThat(result.schedule).isEqualTo(cmd.schedule)
    }

    @Test
    fun `GIVEN not existing show WHEN update by provided cmd THEN should throw exception`() {
        // given
        val cmd = TestShowFactory.updateShowCmd()

        given(showRepository.findByReferenceId(cmd.referenceId)).willReturn(null)

        // when
        val exception =
            assertThrows<EntityNotFoundException> {
                showService.update(cmd)
            }

        // then
        assertThat(exception.message).isEqualTo("Show with referenceId ${cmd.referenceId} not found")
    }

    @Test
    fun `GIVEN existing show WHEN find by referenceId THEN should return show`() {
        // given
        val entity = TestShowFactory.showEntity()

        given(showRepository.findByReferenceId(entity.referenceId)).willReturn(entity)

        // when
        val result = showService.findOne(entity.referenceId)

        // then
        assertThat(result.referenceId).isEqualTo(entity.referenceId)
        assertThat(result.price).isEqualTo(entity.price)
        assertThat(result.schedule).isEqualTo(entity.showScheduleDays.associate { it.date to it.showTimes })
    }

    @Test
    fun `GIVEN not existing show WHEN find by referenceId THEN should throw exception`() {
        // given
        val referenceId = UUID.randomUUID()

        given(showRepository.findByReferenceId(referenceId)).willReturn(null)

        // when
        val exception =
            assertThrows<EntityNotFoundException> {
                showService.findOne(referenceId)
            }

        // then
        assertThat(exception.message).isEqualTo("Show with referenceId $referenceId not found")
    }

    @Test
    fun `GIVEN shows WHEN find all by cmd THEN should return shows`() {
        // given
        val entities =
            listOf(
                TestShowFactory.showEntity(),
                TestShowFactory.showEntity(),
                TestShowFactory.showEntity(),
            )
        val cmd = TestShowFactory.findShowCmd()

        given(showRepository.findAll(any<ShowSpecification>())).willReturn(entities)

        // when
        val result = showService.findAll(cmd)

        // then
        assertThat(result).hasSize(entities.size)
        assertThat(
            result,
        ).extracting("referenceId").containsExactlyInAnyOrderElementsOf(entities.map { it.referenceId })
    }
}
