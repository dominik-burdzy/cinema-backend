package pl.dombur.cinema.domain.show

import pl.dombur.cinema.infrastructure.repository.ShowSpecification
import java.time.LocalDate
import java.util.UUID

data class FindShowCmd(
    val movieReferenceIds: Set<UUID>?,
    val type: ShowType?,
    val showDate: LocalDate?,
) {
    fun toSpecification() =
        ShowSpecification(
            movieReferenceIds = movieReferenceIds,
            type = type,
            showDate = showDate,
        )
}
