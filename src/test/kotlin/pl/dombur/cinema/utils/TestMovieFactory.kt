package pl.dombur.cinema.utils

import pl.dombur.cinema.infrastructure.persistence.MovieEntity
import java.util.UUID

object TestMovieFactory {
    fun movieEntity(
        referenceId: UUID = UUID.randomUUID(),
        imdbId: String = "testId",
        title: String = "Test Movie",
    ) = MovieEntity(
        referenceId = referenceId,
        imdbId = imdbId,
        title = title,
    )
}
