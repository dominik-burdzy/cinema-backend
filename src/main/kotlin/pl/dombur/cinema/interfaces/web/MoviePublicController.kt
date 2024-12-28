package pl.dombur.cinema.interfaces.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.dombur.cinema.application.MovieService
import pl.dombur.cinema.interfaces.web.dto.MovieDetailedResult
import pl.dombur.cinema.interfaces.web.dto.MovieSummaryResult
import java.util.UUID

@RestController
@RequestMapping(MoviePublicController.PATH)
class MoviePublicController(
    private val movieService: MovieService,
) {
    companion object {
        const val PATH = "/api/v1/public/movies"
        const val SPECIFIC_MOVIE_PATH = "/{referenceId}"
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
    fun list(): List<MovieSummaryResult> = movieService.findAll().map { it.toResult() }

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
    ): MovieDetailedResult = movieService.findOne(referenceId).toResult()
}
