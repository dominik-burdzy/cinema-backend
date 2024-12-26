package pl.dombur.cinema.utils

import pl.dombur.cinema.domain.show.FindShowCmd
import pl.dombur.cinema.domain.show.ShowModel
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.domain.show.UpdateShowCmd
import pl.dombur.cinema.infrastructure.persistence.ShowEntity
import pl.dombur.cinema.infrastructure.persistence.ShowScheduleDayEntity
import pl.dombur.cinema.interfaces.web.dto.CreateShowForm
import pl.dombur.cinema.interfaces.web.dto.UpdateShowForm
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

object TestShowFactory {
    fun showEntity(
        referenceId: UUID = UUID.randomUUID(),
        movieReferenceId: UUID = UUID.randomUUID(),
        type: ShowType = ShowType._2D,
    ) = ShowEntity(
        referenceId = referenceId,
        movieReferenceId = movieReferenceId,
        price = BigDecimal(30),
        type = type,
    ).apply {
        showScheduleDays =
            mutableSetOf(
                ShowScheduleDayEntity(
                    date = LocalDate.now(),
                    showTimes = mutableListOf(LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(15, 0)),
                ),
                ShowScheduleDayEntity(
                    date = LocalDate.now().plusDays(1),
                    showTimes = mutableListOf(LocalTime.of(18, 0), LocalTime.of(21, 30)),
                ),
            )
    }

    fun showModel(
        referenceId: UUID = UUID.randomUUID(),
        movieReferenceId: UUID = UUID.randomUUID(),
        price: BigDecimal = BigDecimal(30),
        type: ShowType = ShowType._2D,
        schedule: Map<LocalDate, List<LocalTime>> =
            mapOf(
                LocalDate.now() to listOf(LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(15, 0)),
                LocalDate.now().plusDays(1) to listOf(LocalTime.of(18, 0), LocalTime.of(21, 30)),
            ),
    ) = ShowModel(
        referenceId = referenceId,
        movieReferenceId = movieReferenceId,
        price = price,
        type = type,
        schedule = schedule,
    )

    fun updateShowCmd(referenceId: UUID = UUID.randomUUID()) =
        UpdateShowCmd(
            referenceId = referenceId,
            price = BigDecimal(20),
            schedule =
                mapOf(
                    LocalDate.now() to listOf(LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(16, 0)),
                    LocalDate.now().plusDays(1) to listOf(LocalTime.of(18, 0), LocalTime.of(21, 30)),
                    LocalDate.now().plusDays(2) to listOf(LocalTime.of(16, 0), LocalTime.of(19, 30)),
                ),
        )

    fun findShowCmd() =
        FindShowCmd(
            movieReferenceIds = setOf(UUID.randomUUID()),
            type = ShowType._2D,
            showDate = LocalDate.now(),
        )

    fun createShowForm() =
        CreateShowForm(
            movieReferenceId = UUID.randomUUID(),
            price = BigDecimal(80),
            type = ShowType._4DX,
            schedule =
                mapOf(
                    LocalDate.now() to listOf(LocalTime.of(11, 30), LocalTime.of(13, 0), LocalTime.of(20, 0)),
                    LocalDate.now().plusDays(1) to listOf(LocalTime.of(17, 0), LocalTime.of(21, 30)),
                    LocalDate.now().plusDays(2) to listOf(LocalTime.of(15, 0), LocalTime.of(16, 30)),
                ),
        )

    fun updateShowForm() =
        UpdateShowForm(
            price = BigDecimal(80),
            schedule =
                mapOf(
                    LocalDate.now() to listOf(LocalTime.of(11, 30), LocalTime.of(13, 0), LocalTime.of(20, 0)),
                    LocalDate.now().plusDays(1) to listOf(LocalTime.of(17, 0), LocalTime.of(21, 30)),
                    LocalDate.now().plusDays(2) to listOf(LocalTime.of(15, 0), LocalTime.of(16, 30)),
                ),
        )
}
