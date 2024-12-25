package pl.dombur.cinema.domain.show

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class UpdateShowCmd(
    val referenceId: UUID,
    val price: BigDecimal,
    val schedule: Map<LocalDate, List<LocalTime>>,
)
