package pl.dombur.cinema.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class RateMovieForm(
    @field:Min(1)
    @field:Max(10)
    @Schema(description = "Rating of the movie from 1 to 10", example = "5")
    val rating: Int,
    @Schema(description = "Comment about the movie", example = "Great movie!")
    val comment: String?,
)
