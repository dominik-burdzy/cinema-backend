package pl.dombur.cinema.utils

import pl.dombur.cinema.domain.movie.MovieDetailedModel
import pl.dombur.cinema.domain.movie.MovieModel
import pl.dombur.cinema.infrastructure.external.omdb.MovieData
import pl.dombur.cinema.infrastructure.persistence.MovieEntity
import java.time.LocalDate
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

    fun movieModel(
        referenceId: UUID = UUID.randomUUID(),
        title: String = "Test Movie",
    ) = MovieModel(
        referenceId = referenceId,
        title = title,
    )

    fun movieDetailedModel(
        referenceId: UUID = UUID.randomUUID(),
        title: String = "Test Movie",
        description: String = "Test description",
        releaseDate: LocalDate = LocalDate.of(2021, 1, 1),
        runtime: String = "120 min",
        genre: String = "Action",
        director: String = "Test Director",
        actors: String = "Test Actor",
        country: String = "Test Country",
        imdbRating: String = "8.0",
    ) = MovieDetailedModel(
        referenceId = referenceId,
        title = title,
        description = description,
        releaseDate = releaseDate,
        runtime = runtime,
        genre = genre,
        director = director,
        actors = actors,
        country = country,
        imdbRating = imdbRating,
    )

    fun movieData(
        title: String = "Test Movie",
        description: String = "Test description",
        releaseDate: LocalDate = LocalDate.of(2021, 1, 1),
        runtime: String = "120 min",
        genre: String = "Action",
        director: String = "Test Director",
        actors: String = "Test Actor",
        country: String = "Test Country",
        imdbRating: String = "8.0",
    ) = MovieData(
        title = title,
        description = description,
        releaseDate = releaseDate,
        runtime = runtime,
        genre = genre,
        director = director,
        actors = actors,
        country = country,
        imdbRating = imdbRating,
    )
}
