package pl.dombur.cinema.domain.movie

import pl.dombur.cinema.infrastructure.external.omdb.MovieData
import pl.dombur.cinema.infrastructure.persistence.MovieEntity
import pl.dombur.cinema.interfaces.web.dto.MovieDetailedResult
import java.time.LocalDate
import java.util.UUID

data class MovieDetailedModel(
    val referenceId: UUID,
    val title: String,
    val description: String,
    val releaseDate: LocalDate,
    val runtime: String,
    val genre: String,
    val director: String,
    val actors: String,
    val country: String,
    val imdbRating: String,
) {
    fun toResult() =
        MovieDetailedResult(
            referenceId = referenceId.toString(),
            title = title,
            description = description,
            releaseDate = releaseDate.toString(),
            runtime = runtime,
            genre = genre,
            director = director,
            actors = actors,
            country = country,
            imdbRating = imdbRating,
        )

    companion object {
        fun from(
            entity: MovieEntity,
            data: MovieData,
        ): MovieDetailedModel =
            MovieDetailedModel(
                referenceId = entity.referenceId,
                title = entity.title,
                description = data.description,
                releaseDate = data.releaseDate,
                runtime = data.runtime,
                genre = data.genre,
                director = data.director,
                actors = data.actors,
                country = data.country,
                imdbRating = data.imdbRating,
            )
    }
}
