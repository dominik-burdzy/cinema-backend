package pl.dombur.cinema.infrastructure.external.omdb

import java.time.LocalDate

data class MovieData(
    val title: String,
    val description: String,
    val releaseDate: LocalDate,
    val runtime: String,
    val genre: String,
    val director: String,
    val actors: String,
    val country: String,
    val imdbRating: String,
)
