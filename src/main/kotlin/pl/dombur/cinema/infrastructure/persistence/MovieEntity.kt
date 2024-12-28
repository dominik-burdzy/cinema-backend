package pl.dombur.cinema.infrastructure.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import pl.dombur.cinema.common.persistence.BaseEntity
import java.util.UUID

@Entity
@Table(name = "movie")
class MovieEntity(
    referenceId: UUID? = null,
    var imdbId: String,
    var title: String,
) : BaseEntity(referenceId) {
    @Fetch(FetchMode.JOIN)
    @OneToMany(
        fetch = FetchType.LAZY,
        targetEntity = MovieRatingEntity::class,
        cascade = [CascadeType.ALL],
        mappedBy = "movie",
        orphanRemoval = true,
    )
    var ratings: MutableList<MovieRatingEntity> = mutableListOf()

    fun addRating(rating: MovieRatingEntity) {
        ratings.add(rating)
        rating.movie = this
    }
}
