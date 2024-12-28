package pl.dombur.cinema.infrastructure.external.omdb.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import pl.dombur.cinema.infrastructure.external.omdb.MovieData
import java.time.LocalDate

data class OmdbMovieResponse(
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("Plot")
    val description: String,
    @JsonProperty("Released")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy", locale = "en")
    val releaseDate: LocalDate,
    @JsonProperty("Runtime")
    val runtime: String,
    @JsonProperty("Genre")
    val genre: String,
    @JsonProperty("Director")
    val director: String,
    @JsonProperty("Actors")
    val actors: String,
    @JsonProperty("Country")
    val country: String,
    @JsonProperty("imdbRating")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    val imdbRating: Double,
) {
    fun toMovieData(): MovieData =
        MovieData(
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
