package pl.dombur.cinema.interfaces.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.dombur.cinema.application.ShowService
import pl.dombur.cinema.domain.show.CreateShowCmd
import pl.dombur.cinema.domain.show.UpdateShowCmd
import pl.dombur.cinema.interfaces.web.dto.CreateShowForm
import pl.dombur.cinema.interfaces.web.dto.ShowResult
import pl.dombur.cinema.interfaces.web.dto.UpdateShowForm
import java.util.UUID

@RestController
@RequestMapping(ShowInternalController.PATH)
class ShowInternalController(
    private val showService: ShowService,
) {
    companion object {
        const val PATH = "/api/v1/internal/shows"
        const val SPECIFIC_MOVIE_PATH = "/{referenceId}"
    }

    @Operation(
        summary = "Creates the show details",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "401",
                description = "On invalid input",
            ),
            ApiResponse(
                responseCode = "404",
                description = "If movie related to show doesn't exist",
            ),
            ApiResponse(
                responseCode = "409",
                description = "If show is already defined for given movie and type",
            ),
        ],
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody @Valid form: CreateShowForm,
    ): ShowResult {
        val cmd = CreateShowCmd.fromForm(form)
        return showService.create(cmd).toResult()
    }

    @Operation(
        summary = "Updates the show details",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "On success",
            ),
            ApiResponse(
                responseCode = "401",
                description = "On invalid input",
            ),
            ApiResponse(
                responseCode = "404",
                description = "On movie not found",
            ),
        ],
    )
    @PutMapping(SPECIFIC_MOVIE_PATH)
    fun update(
        @PathVariable referenceId: UUID,
        @RequestBody @Valid form: UpdateShowForm,
    ): ShowResult {
        val cmd =
            UpdateShowCmd(
                referenceId = referenceId,
                price = form.price,
                schedule = form.schedule,
            )
        return showService.update(cmd).toResult()
    }
}
