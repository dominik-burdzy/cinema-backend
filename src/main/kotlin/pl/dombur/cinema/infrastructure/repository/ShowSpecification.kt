package pl.dombur.cinema.infrastructure.repository

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import pl.dombur.cinema.domain.show.ShowType
import pl.dombur.cinema.infrastructure.persistence.ShowEntity
import pl.dombur.cinema.infrastructure.persistence.ShowScheduleDayEntity
import java.time.LocalDate
import java.util.UUID
import kotlin.collections.isNullOrEmpty
import kotlin.collections.toTypedArray

data class ShowSpecification(
    private val movieReferenceIds: Set<UUID>? = null,
    private val type: ShowType? = null,
    private val showDate: LocalDate? = null,
) : Specification<ShowEntity> {
    override fun toPredicate(
        root: Root<ShowEntity>,
        query: CriteriaQuery<*>?,
        cb: CriteriaBuilder,
    ): Predicate? {
        val andConditions = mutableListOf<Predicate>()

        if (!movieReferenceIds.isNullOrEmpty()) {
            andConditions.add(root.get<UUID>(ShowEntity::movieReferenceId.name).`in`(movieReferenceIds))
        }

        if (type != null) {
            andConditions.add(cb.equal(root.get<ShowType>(ShowEntity::type.name), type))
        }

        if (showDate != null) {
            val showScheduleDays: Join<ShowEntity, ShowScheduleDayEntity> = root.join(ShowEntity::showScheduleDays.name)
            val datePath: Path<LocalDate> = showScheduleDays.get(ShowScheduleDayEntity::date.name)
            val dateExpression: Expression<LocalDate> =
                cb.function(
                    ShowScheduleDayEntity::date.name,
                    LocalDate::class.java,
                    datePath,
                )
            andConditions.add(cb.equal(dateExpression, showDate))
        }

        return cb.and(*andConditions.toTypedArray())
    }
}
