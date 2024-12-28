package pl.dombur.cinema.interfaces.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.dombur.cinema.application.MovieService
import pl.dombur.cinema.domain.movie.MovieMapper
import pl.dombur.cinema.domain.movie.RateMovieCmd
import pl.dombur.cinema.interfaces.web.dto.MovieDetailedResult
import pl.dombur.cinema.interfaces.web.dto.MovieSummaryResult
import pl.dombur.cinema.interfaces.web.dto.RateMovieForm
import java.util.UUID

@RestController
@RequestMapping(MoviePublicController.PATH)
class MoviePublicController(
    private val movieService: MovieService,
) {
    companion object {
        const val PATH = "/api/v1/public/movies"
        const val SPECIFIC_MOVIE_PATH = "/{referenceId}"
        const val MOVIE_RATINGS_PATH = "/{referenceId}/ratings"
    }

    @Operation(
        summary = "Get the list of movies",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
        ],
    )
    @GetMapping
    fun list(): List<MovieSummaryResult> = movieService.findAll().map { MovieMapper.toSummaryResult(it) }

    @Operation(
        summary = "Get the specific movie",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "404",
                description = "On movie not found",
            ),
        ],
    )
    @GetMapping(SPECIFIC_MOVIE_PATH)
    fun get(
        @PathVariable referenceId: UUID,
    ): MovieDetailedResult = movieService.findOne(referenceId).let { MovieMapper.toDetailedResult(it) }

    @Operation(
        summary = "Rate the specific movie",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "400",
                description = "On invalid request",
            ),
            ApiResponse(
                responseCode = "404",
                description = "On movie not found",
            ),
        ],
    )
    @PostMapping(MOVIE_RATINGS_PATH)
    fun rate(
        @PathVariable referenceId: UUID,
        @RequestBody @Valid form: RateMovieForm,
    ) {
        val cmd =
            RateMovieCmd(
                referenceId = referenceId,
                rating = form.rating,
                comment = form.comment,
            )
        return movieService.rate(cmd)
    }
}
