package pl.dombur.cinema.domain.movie

import pl.dombur.cinema.infrastructure.persistence.MovieEntity
import pl.dombur.cinema.interfaces.web.dto.MovieSummaryResult
import java.util.UUID

data class MovieModel(
    val referenceId: UUID,
    val title: String,
) {
    fun toResult() =
        MovieSummaryResult(
            referenceId = referenceId,
            title = title,
        )

    companion object {
        fun fromEntity(entity: MovieEntity) =
            MovieModel(
                referenceId = entity.referenceId,
                title = entity.title,
            )
    }
}
