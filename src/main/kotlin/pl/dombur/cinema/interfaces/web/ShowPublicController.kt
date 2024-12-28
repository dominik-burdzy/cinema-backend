package pl.dombur.cinema.interfaces.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.dombur.cinema.application.ShowService
import pl.dombur.cinema.domain.show.FindShowCmd
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.interfaces.web.dto.ShowResult
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping(ShowPublicController.PATH)
class ShowPublicController(
    private val showService: ShowService,
) {
    companion object {
        const val PATH = "/api/v1/public/shows"
        const val SPECIFIC_SHOW_PATH = "/{referenceId}"
    }

    @Operation(
        summary = "Get the list of shows",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "400",
                description = "On invalid input",
            ),
        ],
    )
    @GetMapping
    fun list(
        @RequestParam(required = false)
        @Parameter(description = "Reference IDs of movies related to shows")
        movieReferenceIds: Set<UUID>? = null,
        @RequestParam(required = false)
        @Parameter(description = "Type of show")
        type: ShowType? = null,
        @RequestParam(required = false)
        @Parameter(description = "Date of show")
        showDate: LocalDate? = null,
    ): List<ShowResult> {
        val cmd =
            FindShowCmd(
                movieReferenceIds = movieReferenceIds,
                type = type,
                showDate = showDate,
            )
        return showService.findAll(cmd).map { it.toResult() }
    }

    @Operation(
        summary = "Get the specific show details",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "404",
                description = "On show not found",
            ),
        ],
    )
    @GetMapping(SPECIFIC_SHOW_PATH)
    fun get(
        @PathVariable referenceId: UUID,
    ): ShowResult = showService.findOne(referenceId).toResult()
}
