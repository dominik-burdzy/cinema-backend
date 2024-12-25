package pl.dombur.cinema.domain.show

import pl.dombur.cinema.infrastructure.persistence.ShowEntity
import pl.dombur.cinema.interfaces.web.dto.ShowResult
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class ShowModel(
    val referenceId: UUID,
    val movieReferenceId: UUID,
    val price: BigDecimal,
    val type: ShowType,
    val schedule: Map<LocalDate, List<LocalTime>>,
) {
    fun toResult() =
        ShowResult(
            referenceId = referenceId,
            movieReferenceId = movieReferenceId,
            price = price,
            type = type,
            schedule = schedule,
        )

    companion object {
        fun fromEntity(entity: ShowEntity): ShowModel =
            ShowModel(
                referenceId = entity.referenceId,
                movieReferenceId = entity.movieReferenceId,
                price = entity.price,
                type = entity.type,
                schedule = entity.showScheduleDays.associate { it.date to it.showTimes },
            )
    }
}
