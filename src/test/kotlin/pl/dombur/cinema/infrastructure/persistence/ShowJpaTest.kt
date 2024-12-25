package pl.dombur.cinema.infrastructure.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import pl.dombur.cinema.config.BaseJpaTest
import pl.dombur.cinema.infrastructure.repository.ShowRepository
import pl.dombur.cinema.utils.TestShowFactory
import java.util.UUID

class ShowJpaTest : BaseJpaTest() {
    @Autowired
    private lateinit var showRepository: ShowRepository

    @AfterEach
    fun cleanUp() {
        showRepository.deleteAll()
    }

    @Test
    fun `should save show successfully`() {
        // given
        val entity = TestShowFactory.showEntity()

        // when
        val result = showRepository.save(entity)

        // then
        assertThat(result).isNotNull
        assertThat(result.referenceId).isNotNull
        assertThat(result.price).isEqualTo(entity.price)
        assertThat(result.type).isEqualTo(entity.type)
        assertThat(result.createdAt).isNotNull
        assertThat(result.modifiedAt).isNotNull

        entity.showScheduleDays.forEachIndexed { index, showScheduleDayEntity ->
            assertThat(result.showScheduleDays.elementAt(index).date).isEqualTo(showScheduleDayEntity.date)
            assertThat(result.showScheduleDays.elementAt(index).showTimes).isEqualTo(showScheduleDayEntity.showTimes)
        }
    }

    @Test
    fun `should find show by reference id`() {
        // given
        val showReferenceId = UUID.randomUUID()
        val entity =
            showRepository.save(
                TestShowFactory.showEntity(referenceId = showReferenceId),
            )

        // when
        val result = showRepository.findByReferenceId(showReferenceId)

        // then
        assertThat(result).isNotNull
        assertThat(result).isEqualTo(entity)
    }
}
