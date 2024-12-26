package pl.dombur.cinema.infrastructure.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import pl.dombur.cinema.config.BaseJpaTest
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.infrastructure.repository.ShowRepository
import pl.dombur.cinema.infrastructure.repository.ShowSpecification
import pl.dombur.cinema.utils.TestShowFactory
import java.time.LocalDate
import java.time.LocalTime
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
        assertThat(result.showScheduleDays.associate { it.date to it.showTimes })
            .isEqualTo(entity.showScheduleDays.associate { it.date to it.showTimes })
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

    @Test
    fun `should find all matching shows by specification`() {
        // given
        val showDate = LocalDate.now()
        val type = ShowType.VIP

        val show1 =
            TestShowFactory.showEntity(type = type).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = showDate,
                            showTimes = mutableListOf(LocalTime.of(15, 0)),
                        ),
                    )
            }
        val show2 =
            TestShowFactory.showEntity(type = type).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = showDate,
                            showTimes = mutableListOf(LocalTime.of(12, 0), LocalTime.of(15, 0)),
                        ),
                        ShowScheduleDayEntity(
                            date = showDate.plusDays(1),
                            showTimes = mutableListOf(LocalTime.of(10, 30)),
                        ),
                    )
            }
        val show3 =
            TestShowFactory.showEntity(type = type).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = showDate.minusDays(1),
                            showTimes = mutableListOf(LocalTime.of(12, 0)),
                        ),
                    )
            }
        val show4 =
            TestShowFactory.showEntity(type = ShowType._4DX).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = showDate,
                            showTimes = mutableListOf(LocalTime.of(15, 0)),
                        ),
                    )
            }

        showRepository.saveAll(listOf(show1, show2, show3, show4))

        val movieReferenceIds =
            setOf(
                show2.movieReferenceId,
                show3.movieReferenceId,
                show4.movieReferenceId,
            )

        val specification =
            ShowSpecification(
                movieReferenceIds = movieReferenceIds,
                type = type,
                showDate = showDate,
            )

        // when
        val result = showRepository.findAll(specification)

        // then
        assertThat(result).hasSize(1)
        assertThat(result.first().referenceId).isEqualTo(show2.referenceId)
    }

    @Test
    fun `should find all shows when there are no filters`() {
        // given
        val show1 =
            TestShowFactory.showEntity(type = ShowType._4DX).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = LocalDate.now(),
                            showTimes = mutableListOf(LocalTime.of(15, 0)),
                        ),
                    )
            }
        val show2 =
            TestShowFactory.showEntity(type = ShowType.VIP).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = LocalDate.now(),
                            showTimes = mutableListOf(LocalTime.of(12, 0), LocalTime.of(15, 0)),
                        ),
                        ShowScheduleDayEntity(
                            date = LocalDate.now().plusDays(1),
                            showTimes = mutableListOf(LocalTime.of(10, 30)),
                        ),
                    )
            }
        val show3 =
            TestShowFactory.showEntity(type = ShowType._2D).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = LocalDate.now().minusDays(1),
                            showTimes = mutableListOf(LocalTime.of(12, 0)),
                        ),
                    )
            }
        val show4 =
            TestShowFactory.showEntity(type = ShowType._4DX).apply {
                showScheduleDays =
                    mutableSetOf(
                        ShowScheduleDayEntity(
                            date = LocalDate.now(),
                            showTimes = mutableListOf(LocalTime.of(15, 0)),
                        ),
                    )
            }

        showRepository.saveAll(listOf(show1, show2, show3, show4))

        val specification =
            ShowSpecification(
                movieReferenceIds = null,
                type = null,
                showDate = null,
            )

        // when
        val result = showRepository.findAll(specification)

        // then
        assertThat(result).hasSize(4)
        assertThat(result)
            .extracting("referenceId")
            .containsExactlyInAnyOrder(show1.referenceId, show2.referenceId, show3.referenceId, show4.referenceId)
    }
}
