package pl.dombur.cinema.infrastructure.persistence

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import pl.dombur.cinema.common.persistence.BaseEntity
import java.util.UUID

@Entity
@Table(name = "movie_rating")
class MovieRatingEntity(
    referenceId: UUID? = null,
    var rating: Int,
    var comment: String?,
) : BaseEntity(referenceId) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_reference_id")
    var movie: MovieEntity? = null
}
