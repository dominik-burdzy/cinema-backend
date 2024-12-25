package pl.dombur.cinema.infrastructure.repository

import org.springframework.stereotype.Repository
import pl.dombur.cinema.common.persistence.BaseRepository
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.infrastructure.persistence.ShowEntity
import java.util.UUID

@Repository
interface ShowRepository : BaseRepository<ShowEntity> {
    fun existsByMovieReferenceIdAndType(
        movieReferenceId: UUID,
        type: ShowType,
    ): Boolean
}
