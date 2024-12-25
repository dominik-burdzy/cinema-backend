package pl.dombur.cinema.interfaces.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class UpdateShowForm(
    @Schema(description = "Price of the show")
    val price: BigDecimal,
    @Schema(
        description = "Schedule of the show with dates and times",
        example = "{ \"2022-12-31\": [\"10:30\", \"12:00\", \"15:00\"], \"2023-01-01\": [\"18:00\", \"21:30\"] }",
    )
    val schedule: Map<LocalDate, List<LocalTime>>,
)
