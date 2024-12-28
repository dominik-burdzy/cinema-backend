package pl.dombur.cinema.application

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.dombur.cinema.domain.movie.MovieDataProvider
import pl.dombur.cinema.domain.movie.MovieDetailedModel
import pl.dombur.cinema.domain.movie.MovieModel
import pl.dombur.cinema.infrastructure.repository.MovieRepository
import java.util.UUID

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val movieDataProvider: MovieDataProvider,
) {
    @Transactional(readOnly = true)
    fun findOne(referenceId: UUID): MovieDetailedModel {
        val entity =
            movieRepository.findByReferenceId(referenceId)
                ?: throw EntityNotFoundException("Movie with referenceId: $referenceId not found")

        val data = movieDataProvider.getMovie(entity.imdbId)

        return MovieDetailedModel.from(
            entity = entity,
            data = data,
        )
    }

    @Transactional(readOnly = true)
    fun findAll(): List<MovieModel> =
        movieRepository
            .findAll()
            .map { MovieModel.fromEntity(it) }
}
