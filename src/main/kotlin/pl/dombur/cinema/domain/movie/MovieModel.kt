package pl.dombur.cinema.domain.movie

import pl.dombur.cinema.infrastructure.persistence.MovieEntity
import java.util.UUID

data class MovieModel(
    val referenceId: UUID,
    val title: String,
) {
    companion object {
        fun fromEntity(entity: MovieEntity) =
            MovieModel(
                referenceId = entity.referenceId,
                title = entity.title,
            )
    }
}
