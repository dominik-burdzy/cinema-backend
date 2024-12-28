package pl.dombur.cinema.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

data class MovieSummaryResult(
    @Schema(description = "Reference ID of the movie")
    val referenceId: UUID,
    @Schema(description = "Title of the movie")
    val title: String,
)
