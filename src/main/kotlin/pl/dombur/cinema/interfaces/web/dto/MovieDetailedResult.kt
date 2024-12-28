package pl.dombur.cinema.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.UUID

data class MovieDetailedResult(
    @Schema(description = "Reference ID of the movie")
    val referenceId: UUID,
    @Schema(description = "Title of the movie")
    val title: String,
    @Schema(description = "Description of the movie")
    val description: String,
    @Schema(description = "Release date of the movie")
    val releaseDate: LocalDate,
    @Schema(description = "Runtime of the movie")
    val runtime: String,
    @Schema(description = "Genre of the movie")
    val genre: String,
    @Schema(description = "Director of the movie")
    val director: String,
    @Schema(description = "Actors of the movie")
    val actors: String,
    @Schema(description = "Country where the movie was made")
    val country: String,
    @Schema(description = "IMDB rating of the movie")
    val imdbRating: Double,
    @Schema(description = "Cinema rating of the movie")
    val cinemaRating: Double?,
)
