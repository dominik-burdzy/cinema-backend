package pl.dombur.cinema.domain.movie

import java.util.UUID

data class RateMovieCmd(
    val referenceId: UUID,
    val rating: Int,
    val comment: String?,
)
