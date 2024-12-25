package pl.dombur.cinema.application.exception

import pl.dombur.cinema.domain.show.ShowType
import java.util.UUID

class ShowAlreadyDefinedException(
    movieReferenceId: UUID,
    showType: ShowType,
) : RuntimeException(
        "Show for movie: $movieReferenceId and type: $showType has been already defined.",
    )
