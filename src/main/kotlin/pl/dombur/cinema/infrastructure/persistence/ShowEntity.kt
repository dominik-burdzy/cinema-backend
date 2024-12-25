package pl.dombur.cinema.infrastructure.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import pl.dombur.cinema.common.persistence.BaseEntity
import pl.dombur.cinema.domain.show.ShowType
import java.math.BigDecimal
import java.util.UUID
import kotlin.text.clear

@Entity
@Table(name = "show")
class ShowEntity(
    referenceId: UUID? = null,
    var movieReferenceId: UUID,
    var price: BigDecimal,
    @Enumerated(EnumType.STRING)
    var type: ShowType,
) : BaseEntity(referenceId) {
    @Fetch(FetchMode.JOIN)
    @OneToMany(
        targetEntity = ShowScheduleDayEntity::class,
        cascade = [CascadeType.ALL],
        mappedBy = "show",
        orphanRemoval = true,
    )
    var showScheduleDays: MutableSet<ShowScheduleDayEntity> = mutableSetOf()
        set(value) {
            field.clear()
            value.forEach(this::addScheduleDay)
        }

    private fun addScheduleDay(day: ShowScheduleDayEntity) {
        showScheduleDays.add(day)
        day.show = this
    }
}
