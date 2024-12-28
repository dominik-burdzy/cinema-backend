package pl.dombur.cinema.domain.movie

import pl.dombur.cinema.infrastructure.external.omdb.MovieData
import pl.dombur.cinema.infrastructure.persistence.MovieEntity
import pl.dombur.cinema.infrastructure.persistence.MovieRatingEntity
import pl.dombur.cinema.interfaces.web.dto.MovieDetailedResult
import pl.dombur.cinema.interfaces.web.dto.MovieSummaryResult
import java.math.RoundingMode

object MovieMapper {
    fun toDetailedResult(model: MovieDetailedModel) =
        MovieDetailedResult(
            referenceId = model.referenceId,
            title = model.title,
            description = model.description,
            releaseDate = model.releaseDate,
            runtime = model.runtime,
            genre = model.genre,
            director = model.director,
            actors = model.actors,
            country = model.country,
            imdbRating = model.imdbRating,
            cinemaRating = model.cinemaRating,
        )

    fun toSummaryResult(model: MovieModel) =
        MovieSummaryResult(
            referenceId = model.referenceId,
            title = model.title,
        )

    fun toDetailedModel(
        entity: MovieEntity,
        data: MovieData,
    ) = MovieDetailedModel(
        referenceId = entity.referenceId,
        title = data.title,
        description = data.description,
        releaseDate = data.releaseDate,
        runtime = data.runtime,
        genre = data.genre,
        director = data.director,
        actors = data.actors,
        country = data.country,
        imdbRating = data.imdbRating,
        cinemaRating = calculateRating(entity.ratings),
    )

    private fun calculateRating(ratings: List<MovieRatingEntity>): Double? =
        ratings
            .takeIf { it.isNotEmpty() }
            ?.map { it.rating }
            ?.average()
            ?.round()

    private fun Double.round() = this.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
}
