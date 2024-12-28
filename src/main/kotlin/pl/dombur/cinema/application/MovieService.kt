package pl.dombur.cinema.application

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.dombur.cinema.domain.movie.MovieDataProvider
import pl.dombur.cinema.domain.movie.MovieDetailedModel
import pl.dombur.cinema.domain.movie.MovieMapper
import pl.dombur.cinema.domain.movie.MovieModel
import pl.dombur.cinema.domain.movie.RateMovieCmd
import pl.dombur.cinema.infrastructure.persistence.MovieRatingEntity
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

        return MovieMapper.toDetailedModel(
            entity = entity,
            data = data,
        )
    }

    @Transactional(readOnly = true)
    fun findAll(): List<MovieModel> =
        movieRepository
            .findAll()
            .map { MovieModel.fromEntity(it) }

    @Transactional
    fun rate(cmd: RateMovieCmd) {
        val movie =
            movieRepository.findByReferenceId(cmd.referenceId)
                ?: throw EntityNotFoundException("Movie with referenceId: ${cmd.referenceId} not found")

        val rating =
            MovieRatingEntity(
                rating = cmd.rating,
                comment = cmd.comment,
            )
        movie.addRating(rating)

        movieRepository.save(movie)
    }
}
