package pl.dombur.cinema.application

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.dombur.cinema.application.exception.ShowAlreadyDefinedException
import pl.dombur.cinema.domain.show.CreateShowCmd
import pl.dombur.cinema.domain.show.FindShowCmd
import pl.dombur.cinema.domain.show.ShowModel
import pl.dombur.cinema.domain.show.UpdateShowCmd
import pl.dombur.cinema.infrastructure.persistence.ShowScheduleDayEntity
import pl.dombur.cinema.infrastructure.repository.ShowRepository
import java.util.UUID

@Service
class ShowService(
    private val showRepository: ShowRepository,
) {
    @Transactional(readOnly = true)
    fun findOne(referenceId: UUID): ShowModel =
        showRepository
            .findByReferenceId(referenceId)
            ?.let { ShowModel.fromEntity(it) }
            ?: throw EntityNotFoundException("Show with referenceId $referenceId not found")

    @Transactional(readOnly = true)
    fun findAll(cmd: FindShowCmd): List<ShowModel> {
        val spec = cmd.toSpecification()
        return showRepository
            .findAll(spec)
            .map { ShowModel.fromEntity(it) }
    }

    @Transactional
    fun create(cmd: CreateShowCmd): ShowModel {
        validate(cmd)
        return showRepository
            .save(cmd.toEntity())
            .let { ShowModel.fromEntity(it) }
    }

    @Transactional
    fun update(cmd: UpdateShowCmd): ShowModel {
        val entity =
            showRepository.findByReferenceId(cmd.referenceId)
                ?: throw EntityNotFoundException("Show with referenceId ${cmd.referenceId} not found")

        entity.price = cmd.price
        entity.showScheduleDays =
            cmd.schedule
                .map { showDay ->
                    ShowScheduleDayEntity(
                        date = showDay.key,
                        showTimes = showDay.value.toMutableList(),
                    )
                }.toMutableSet()

        return showRepository
            .save(entity)
            .let { ShowModel.fromEntity(it) }
    }

    private fun validate(cmd: CreateShowCmd) {
        if (showRepository.existsByMovieReferenceIdAndType(cmd.movieReferenceId, cmd.type)) {
            throw ShowAlreadyDefinedException(cmd.movieReferenceId, cmd.type)
        }
    }
}
