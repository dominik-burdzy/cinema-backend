package pl.dombur.cinema.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import pl.dombur.cinema.domain.show.ShowType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class ShowResult(
    @Schema(description = "Reference ID of the show")
    val referenceId: UUID,
    @Schema(description = "Reference ID of the movie for which the show is created")
    val movieReferenceId: UUID,
    @Schema(description = "Price of the show")
    val price: BigDecimal,
    @Schema(
        description = "Type of the show",
        example = "_2D",
    )
    val type: ShowType,
    @Schema(
        description = "Schedule of the show with dates and times",
        example = "{ \"2022-12-31\": [\"10:30\", \"12:00\", \"15:00\"], \"2023-01-01\": [\"18:00\", \"21:30\"] }",
    )
    val schedule: Map<LocalDate, List<LocalTime>>,
)
