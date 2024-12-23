package pl.dombur.cinema.infrastructure.persistence

import jakarta.persistence.Entity
import jakarta.persistence.Table
import pl.dombur.cinema.common.persistence.BaseEntity
import java.util.UUID

@Entity
@Table(name = "movie")
class MovieEntity(
    referenceId: UUID? = null,
    var imdbId: String,
    var title: String,
) : BaseEntity(referenceId)
