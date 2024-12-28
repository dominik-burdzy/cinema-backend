package pl.dombur.cinema.application

import jakarta.persistence.EntityNotFoundException
import mu.KotlinLogging
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

private val logger = KotlinLogging.logger {}

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val movieDataProvider: MovieDataProvider,
) {
    @Transactional(readOnly = true)
    fun findOne(referenceId: UUID): MovieDetailedModel {
        logger.info { "Finding movie with referenceId $referenceId" }
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
    fun findAll(): List<MovieModel> {
        logger.info { "Finding all movies" }
        return movieRepository
            .findAll()
            .map { MovieModel.fromEntity(it) }
    }

    @Transactional
    fun rate(cmd: RateMovieCmd) {
        logger.info { "Rating movie with referenceId ${cmd.referenceId} with rating ${cmd.rating}" }
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
