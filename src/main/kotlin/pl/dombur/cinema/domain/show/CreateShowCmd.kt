package pl.dombur.cinema.domain.show

import pl.dombur.cinema.infrastructure.persistence.ShowEntity
import pl.dombur.cinema.infrastructure.persistence.ShowScheduleDayEntity
import pl.dombur.cinema.interfaces.web.dto.CreateShowForm
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class CreateShowCmd(
    val movieReferenceId: UUID,
    val price: BigDecimal,
    val type: ShowType,
    val schedule: Map<LocalDate, List<LocalTime>>,
) {
    fun toEntity(): ShowEntity =
        ShowEntity(
            movieReferenceId = movieReferenceId,
            price = price,
            type = type,
        ).apply {
            showScheduleDays =
                schedule
                    .map { showDay ->
                        ShowScheduleDayEntity(
                            date = showDay.key,
                            showTimes = showDay.value.toMutableList(),
                        )
                    }.toMutableSet()
        }

    companion object {
        fun fromForm(form: CreateShowForm) =
            CreateShowCmd(
                movieReferenceId = form.movieReferenceId,
                price = form.price,
                type = form.type,
                schedule = form.schedule,
            )
    }
}
