package pl.dombur.cinema.domain.movie

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
    val imdbRating: Double,
    val cinemaRating: Double?,
)
