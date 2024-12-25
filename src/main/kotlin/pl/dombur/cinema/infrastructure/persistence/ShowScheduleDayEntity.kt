package pl.dombur.cinema.infrastructure.persistence

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import pl.dombur.cinema.common.persistence.BaseEntity
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "show_schedule_day")
class ShowScheduleDayEntity(
    referenceId: UUID? = null,
    var date: LocalDate,
    @Column(name = "time")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "show_schedule_time", joinColumns = [JoinColumn(name = "show_schedule_day_reference_id")])
    var showTimes: MutableList<LocalTime> = mutableListOf(),
) : BaseEntity(referenceId) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_reference_id")
    var show: ShowEntity? = null
}
